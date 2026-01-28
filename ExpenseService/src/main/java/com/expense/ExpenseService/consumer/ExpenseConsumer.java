package com.expense.ExpenseService.consumer;

import com.expense.ExpenseService.dto.ExpenseDto;
import com.expense.ExpenseService.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ExpenseConsumer {

    private ExpenseService expenseService;

    @Autowired
    ExpenseConsumer(ExpenseService expenseService){
        this.expenseService = expenseService;
    }

    @KafkaListener(topics = "${spring.kafka.topic-json.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void listens(ExpenseDto expenseDto){
        try{
            // Todo: Make it transactional, and check if duplicate event (Handle idempotency)
            expenseService.createExpense(expenseDto);
        } catch (Exception e) {
            System.out.println("Exception in listening the event");
        }
    }
}