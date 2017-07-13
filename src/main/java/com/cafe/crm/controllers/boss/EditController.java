package com.cafe.crm.controllers.boss;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.cafe.crm.configs.property.AdvertisingCustomSettings;
import com.cafe.crm.models.advertising.AdvertisingSettings;
import com.cafe.crm.models.client.Board;
import com.cafe.crm.models.client.Calculate;
import com.cafe.crm.models.property.PropertyWrapper;
import com.cafe.crm.services.interfaces.advertising.AdvertisingSettingsService;
import com.cafe.crm.services.interfaces.board.BoardService;
import com.cafe.crm.services.interfaces.calculate.CalculateService;
import com.cafe.crm.services.interfaces.property.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/boss")
public class EditController {

	@Autowired
	private PropertyService propertyService;

	@Autowired
	private BoardService boardService;

	@Autowired
	private CalculateService calculateService;

	@Autowired
	AdvertisingSettingsService advertisingSettingsService;

	@Autowired
	AdvertisingCustomSettings customSettings;

	@Autowired
	@Qualifier(value = "logger")
	private Logger log;

	@Autowired
	@Qualifier(value = "hibLogger")
	private Logger hibLog;

	@ModelAttribute(name = "advertSettings")
	public JavaMailSenderImpl senderSettings() {
		return customSettings.getCustomSettings();
	}

	@ModelAttribute(name = "listSMTPSettings")
	public List<AdvertisingSettings> listSettings() {
		return advertisingSettingsService.getAll();
	}

	@ModelAttribute(name = "logLevel")
	public String logLevel() {
		return log.getLevel().levelStr;
	}

	@ModelAttribute(value = "wrapper")
	public PropertyWrapper addClass() {
		PropertyWrapper PropertyWrapper = new PropertyWrapper();
		PropertyWrapper.setProperties(propertyService.findAll());
		return PropertyWrapper;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView mainPage() {
		ModelAndView modelAndView = new ModelAndView("bossSettingsMenu");
		modelAndView.addObject("boards", boardService.getAll());
		return modelAndView;
	}

	@RequestMapping(value = "/property/edit", method = RequestMethod.POST)
	public String map(@ModelAttribute PropertyWrapper wrapper, HttpServletRequest request) {
		propertyService.saveCollection(wrapper.getProperties());
		String referrer = request.getHeader("Referer");
		return "redirect:" + referrer;
	}

	@RequestMapping(value = "/board/edit", method = RequestMethod.GET)
	public ModelAndView getAllBoard() {
		ModelAndView modelAndView = new ModelAndView("pages/editBoard");
		modelAndView.addObject("boards", boardService.getAll());
		return modelAndView;
	}

	@RequestMapping(value = "/board/edit", method = RequestMethod.POST)
	public String editBoard(Board board, HttpServletRequest request) {
		Board br = boardService.getOne(board.getId());
		if (br != null) {
			boardService.save(board);
		}
		String referrer = request.getHeader("Referer");
		return "redirect:" + referrer;
	}

	@RequestMapping(value = "/board/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Long deleteBoard(@PathVariable("id") Long id, HttpServletRequest request) {
		String referer = request.getHeader("Referer");
		List<Calculate> calc = calculateService.getAllOpen();
		for (Calculate calculate : calc) {
			if (calculate.getBoard().getId().equals(id)) {
				return -1L;//Этот стол открыт
			} else {
				boardService.deleteById(id);
				calculate.getBoard().setIsOpen(false);
			}
		}
		return id;
	}

	@RequestMapping(value = "/board/new", method = RequestMethod.POST)
	public String newBoard(Board board, HttpServletRequest request) {
		boardService.save(board);
		String referrer = request.getHeader("Referer");
		return "redirect:" + referrer;
	}

	@ResponseBody
	@RequestMapping(value = "/property/logLevel", method = RequestMethod.POST)
	public ResponseEntity<?> setLogLevel(@RequestParam(name = "level") String logLevel) {
		log.info("Log level: " + logLevel);
		switch (logLevel) {
			case ("ERROR"):
				log.setLevel(Level.ERROR);
				hibLog.setLevel(Level.ERROR);
				break;
			case ("INFO"):
				log.setLevel(Level.INFO);
				hibLog.setLevel(Level.INFO);
				break;
			case ("DEBUG"):
				log.setLevel(Level.DEBUG);
				hibLog.setLevel(Level.DEBUG);
				break;
			case ("WARN"):
				log.setLevel(Level.WARN);
				hibLog.setLevel(Level.WARN);
				break;
			default:
				log.error("Unknown log level: " + logLevel);
				break;
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value = "/property/advertising-settings", method = RequestMethod.POST)
	public String setAdvertisingCustomSettings(
			@RequestParam(name = "settingsName")String settingsName,
			@RequestParam(name = "password")String password,
			@RequestParam(name = "email")String email,
			HttpServletRequest request) {

		String referer = request.getHeader("Referer");

		AdvertisingSettings newSettings = new AdvertisingSettings(settingsName, email, password, "smtp.gmail.com");

		advertisingSettingsService.save(newSettings);

		JavaMailSenderImpl senderImpl = customSettings.getCustomSettings();
		senderImpl.setUsername(email);
		senderImpl.setPassword(password);

		return "redirect:" + referer;
	}

}