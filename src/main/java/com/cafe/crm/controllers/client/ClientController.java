package com.cafe.crm.controllers.client;

import com.cafe.crm.dao.client.CalculateRepository;
import com.cafe.crm.dao.client.ClientRepository;
import com.cafe.crm.models.client.Calculate;
import com.cafe.crm.models.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Controller
public class ClientController {
    @Autowired
    ClientRepository clientRepository;

    @Autowired
    CalculateRepository calculateRepository;


    @RequestMapping(value = {"/get-all-clients-calculates"}, method = RequestMethod.GET)
    public ModelAndView getAllClients(ModelMap modelMap) {
        modelMap.addAttribute("listClient", clientRepository.findAll());
        modelMap.addAttribute("listCalculate", calculateRepository.findAll());
        return new ModelAndView("clients");
    }

    @RequestMapping(value = {"/add-calculate"}, method = RequestMethod.POST)
    public void addCalculate(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) throws Exception {
        Client client = null;
        Calculate calculate = new Calculate();

        if (request.getParameter("ClientId") != null) {
            client = clientRepository.findOne(Long.parseLong(request.getParameter("ClientId")));
            calculate.setName(client.getName());
            calculate.setSurname(client.getSurname());
        } else {
            calculate.setName(request.getParameter("anonName")+"-Анон");
            calculate.setSurname(request.getParameter("anonSurname")+"-Анон");
        }
        calculate.setTimeStart(new Date());
        calculate.setTimeStop(new Date());
        calculate.setClient(client);
        calculate.setMenu("туточки заказ");
        calculate.setTimePrice((long)123);
        calculate.setAllPrice((long)1111);
        calculateRepository.save(calculate);

        response.sendRedirect("/get-all-clients-calculates");
    }

}
