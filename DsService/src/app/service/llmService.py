from typing import Optional
from langchain_core.prompts import ChatPromptTemplate, MessagesPlaceholder
from pydantic import BaseModel, Field
from langchain_openai import ChatOpenAI
from langchain_mistralai import ChatMistralAI

from service.Expense import Expense
from langchain_core.utils.function_calling import convert_to_openai_tool
from dotenv import load_dotenv, dotenv_values
import os

class LLMService:
    def __init__(self):
        load_dotenv()
        self.prompt = ChatPromptTemplate.from_messages(
            [
                (
                    "system", 
                    "You are an expert extraction algorithm. "
                    "Only Extract the relevant information from the text. "
                    "If you do not know the value of an attribute asked to extract, "
                    "return null for that attribute's value."
                ),
                ("human", "{text}")
            ]
        )
        self.apiKey = os.getenv("OPENAI_API_KEY")
        self.llm = ChatMistralAI(api_key = self.apiKey, model="mistral-large-latest")
        self.runnable = self.prompt | self.llm.with_structured_output(schema = Expense)
    
    def runLLM(self, mesage):
        return self.runnable.invoke({"text": mesage})