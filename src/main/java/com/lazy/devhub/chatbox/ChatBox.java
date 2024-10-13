package com.lazy.devhub.chatbox;
import java.io.File;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ChatBox extends TelegramLongPollingBot {
    private String bot_username;
	private String bot_token;
	public ChatBox(String username,String token){
		this.bot_username = username;
		this.bot_token = token;
	}
	


	@Override
	public String getBotUsername() {
		return bot_username; // Tên bot của bạn
	}

	@Override
	public String getBotToken() {
		return bot_token; // API token của bot từ BotFather
	}

	public void sendFile(String chatid, File file) throws Exception{
		if (bot_username == null || bot_token == null) {
			throw new Exception("Login Bot Error");
		}
		SendDocument sendDocument = new SendDocument();
		sendDocument.setChatId(chatid);
		
		InputFile inputFile = new InputFile(file);
		sendDocument.setDocument(inputFile);

		try {
			// Gửi tài liệu
			execute(sendDocument);
			System.out.println("Document sent successfully.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		
	}

	@Override
	public void onUpdateReceived(Update arg0) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'onUpdateReceived'");
	}

}