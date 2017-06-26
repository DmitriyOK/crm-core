package com.cafe.crm.controllers.calculate;

import com.cafe.crm.models.client.Client;
import com.cafe.crm.service_abstract.boardService.BoardService;
import com.cafe.crm.service_abstract.calculateService.CalculateControllerService;
import com.cafe.crm.service_abstract.calculateService.CalculateService;
import com.cafe.crm.service_abstract.cardService.CardService;
import com.cafe.crm.service_abstract.clientService.ClientService;
import com.cafe.crm.service_abstract.menu.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@Controller
@RequestMapping("/manager")
public class CalculateController {

	@Autowired
	private ClientService clientService;

	@Autowired
	private CalculateControllerService calculateControllerService;

	@Autowired
	private CalculateService calculateService;

	@Autowired
	private BoardService boardService;

	@Autowired
	private MenuService menuService;

	@Autowired
	private CardService cardService;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView manager() {
		ModelAndView modelAndView = new ModelAndView("clients");
		modelAndView.addObject("listBoard", boardService.getAll());
		modelAndView.addObject("listCalculate", calculateService.getAllOpen());
		modelAndView.addObject("listMenu", menuService.getOne(1L));
		return modelAndView;
	}

	@RequestMapping(value = {"/add-calculate"}, method = RequestMethod.POST)
	public String createCalculate(@RequestParam("boardId") Long id,
								  @RequestParam("number") Double number,
								  @RequestParam("description") String description) {
		calculateControllerService.createCalculate(id, number.longValue(), description);
		return "redirect:/manager";
	}


	@RequestMapping(value = {"/add-card-on-client"}, method = RequestMethod.POST)
	@ResponseBody
	public Long addCardOnClient(@RequestParam("calculateId") Long calculateId,
								@RequestParam("clientId") Long clientId,
								@RequestParam("cardId") Long cardId) {
		return calculateControllerService.addCardOnClient(calculateId, clientId, cardId);
	}

	@RequestMapping(value = {"/refresh-board"}, method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void refreshBoard(@RequestParam("boardId") Long idB,
							 @RequestParam("calculateId") Long idC) {
		calculateControllerService.refreshBoard(idC, idB);
	}

	@RequestMapping(value = {"/add-client"}, method = RequestMethod.POST)
	public String addClient(@RequestParam("calculateId") Long id,
							@RequestParam("number") Double number,
							@RequestParam("description") String description) {
		calculateControllerService.addClient(id, number.longValue(), description);
		return "redirect:/manager";
	}

	@RequestMapping(value = {"/update-fields-client"}, method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void UpdateFieldsClient(@RequestParam("clientId") Long clientId,
								   @RequestParam("discount") Double discount,
								   @RequestParam("payWithCard") Double payWithCard,
								   @RequestParam("description") String description) {
		Client client = clientService.getOne(clientId);
		client.setDiscount(discount.longValue());
		client.setPayWithCard(payWithCard.longValue());
		client.setDescription(description);
		clientService.save(client);
	}

	@RequestMapping(value = {"/calculate-price"}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Client> calculatePrice() {
		return calculateControllerService.calculatePrice();
	}

	@RequestMapping(value = {"/output-clients"}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Client> outputClients(@RequestParam(name = "clientsId", required = false) Long[] clientsId) {
		return calculateControllerService.outputClients(clientsId);
	}

	@RequestMapping(value = {"/close-client"}, method = RequestMethod.POST)
	public String closeClient(@RequestParam(name = "clientsId", required = false) Long[] clientsId,
							  @RequestParam("calculateId") Long calculateId) {
		calculateControllerService.closeClient(clientsId, calculateId);
		return "redirect:/manager";
	}

}

