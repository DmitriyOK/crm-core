package com.cafe.crm.dao_impl.client.calculateService;

import com.cafe.crm.dao_impl.client.CalculateService;
import com.cafe.crm.dao_impl.client.CardService;
import com.cafe.crm.models.client.Calculate;
import com.cafe.crm.models.client.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CardControllerService {

	@Autowired
	private CardService cardService;

	@Autowired
	private CalculateService calculateService;

	public void   addCardToCalculate(Long idCard,Long idCalculate) {
		Card card = cardService.getOne(idCard); //обновляем дату последнего посещения
		card.setVisitDate(LocalDate.now());
		cardService.add(card);

		Calculate calculate = calculateService.getOne(idCalculate);
		calculate.setCard(card);
		calculateService.add(calculate);
	}

}
