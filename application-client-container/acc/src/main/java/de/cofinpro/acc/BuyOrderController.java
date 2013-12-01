package de.cofinpro.acc;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class BuyOrderController extends AnchorPane implements Initializable {

	@FXML
	TextField security;
	
	@FXML
	TextField rate;
	
	@FXML
	TextField count;
	
	@FXML
	TextField orderValue;
	
	@FXML
	Button buy;
	
	@FXML
	Button cancel;

	private Security selectedSecurity;
	private Depot depot;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}

	public void showSecurityToBuy(Security selectedSecurity) {
		this.selectedSecurity = selectedSecurity;
		// TODO show date
		security.setText(selectedSecurity.getIsin());
		rate.setText(Float.toString(selectedSecurity.getLatestRate().getRate()));
	}
	
	public void setDepot(Depot depot) {
		this.depot = depot;
	}

	public void updateOrderValue(KeyEvent event) {
		BigDecimal currentOrderValue = new BigDecimal(0);
		if(!count.getText().isEmpty()) {
			BigDecimal currentCount = new BigDecimal(count.getText());
			BigDecimal currentRate = new BigDecimal(rate.getText());
			currentOrderValue = currentCount.multiply(currentRate);
		}
		orderValue.setText(currentOrderValue.toPlainString());
		
		updateBuyButton(currentOrderValue);
	}
	
	private void updateBuyButton(BigDecimal currentOrderValue) {
		buy.setDisable(ifNotEnoughMoneyAvailable(currentOrderValue));
	}

	private boolean ifNotEnoughMoneyAvailable(BigDecimal currentOrderValue) {
		BigDecimal balance = depot.getBalance();
		return currentOrderValue.compareTo(balance) > 0;
	}

	public void buy(ActionEvent event) {
		System.out.println("BuyOrderController.buy()");
		
		depot = Main.trader.buy(depot, selectedSecurity, Integer.valueOf(count.getText()));
		System.out.println(depot.getBalance());
		
		showTradeViewDialog();
	}
	
	public void cancel(ActionEvent event) {
		System.out.println("BuyOrderController.cancel()");
		
		showTradeViewDialog();
	}

	private void showTradeViewDialog() {
		Stage stage = (Stage) cancel.getScene().getWindow();
		SceneLoader sceneLoader = new SceneLoader();
		sceneLoader.load("TradeView.fxml");
		Scene tradeViewScene = sceneLoader.getScene();
		TradeViewController tradeViewController = sceneLoader.getController();
		tradeViewController.setDepot(depot);
		tradeViewController.updateDepot();
		stage.setScene(tradeViewScene);
	}
	
}
