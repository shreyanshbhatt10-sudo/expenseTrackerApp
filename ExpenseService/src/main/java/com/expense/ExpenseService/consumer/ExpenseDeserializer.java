package com.expense.ExpenseService.consumer;

import com.expense.ExpenseService.dto.ExpenseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;

public class ExpenseDeserializer implements Deserializer<ExpenseDto> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Deserializer.super.configure(configs, isKey);
    }

    @Override
    public ExpenseDto deserialize(String arg0, byte[] arg1) {
        ObjectMapper objectMapper = new ObjectMapper();
        ExpenseDto expenseDto = null;
        try{
            expenseDto = objectMapper.readValue(arg1, ExpenseDto.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return expenseDto;
    }


    @Override
    public void close() {
        Deserializer.super.close();
    }
}
