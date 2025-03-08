package fr.example.configurations;

public interface ConfigurationSender {

	boolean post(String url, String body);

}