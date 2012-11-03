package com.kt.kbp.paypal;

import java.io.Serializable;
import java.math.BigDecimal;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.google.android.apps.analytics.Transaction;
import com.google.android.apps.analytics.Transaction.Builder;
import com.kt.kbp.R;
import com.kt.kbp.common.Constants;
import com.kt.kbp.common.ExceptionTrackerInterface;
import com.kt.kbp.path.Path;
import com.kt.kbp.path.PathInterface;
import com.paypal.android.MEP.CheckoutButton;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalInvoiceData;
import com.paypal.android.MEP.PayPalInvoiceItem;
import com.paypal.android.MEP.PayPalPayment;
import com.paypal.android.MEP.PayPalResultDelegate;

public class PaypalFragment extends Fragment implements ExceptionTrackerInterface, PayPalResultDelegate, PathInterface, Serializable {

	private static final long serialVersionUID = 6847175698452844410L;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	super.onCreateView(inflater, container, savedInstanceState);
    	return inflater.inflate(R.layout.fragment_paypal, container, false);
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	Log.i("fragments", "resuming PaypalFragment");
        PayPal payPalObject = PayPal.initWithAppID(getActivity(), Constants.PAYPAL_SANDBOX, PayPal.ENV_SANDBOX);
        //PayPal payPalObject = PayPal.initWithAppID(this, Constants.PAYPAL_LIVE, PayPal.ENV_LIVE);
        getCheckoutButton(payPalObject);
    }
    
    public void getCheckoutButton(PayPal payPalObject) {
    	CheckoutButton launchPayPalButton = payPalObject.getCheckoutButton(getActivity(), PayPal.BUTTON_278x43, CheckoutButton.TEXT_DONATE);
    	launchPayPalButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				payPalButtonClick();
			}
		});
    	((RelativeLayout)getActivity().findViewById(R.id.donate_button)).addView(launchPayPalButton);
    }
    
    public void payPalButtonClick() {
    	GoogleAnalyticsTracker.getInstance().trackEvent("Paypal", "PayPalButton", "Click", 0);
		
		PayPalPayment payment = new PayPalPayment();

		payment.setCurrencyType("USD");
		payment.setRecipient(Constants.PAYPAL_SANDBOX_EMAIL);
		//payment.setRecipient(Constants.PAYPAL_LIVE_EMAIL);
		payment.setSubtotal(new BigDecimal(5));

		payment.setPaymentType(PayPal.PAYMENT_TYPE_NONE);

		PayPalInvoiceData invoice = new PayPalInvoiceData();

		PayPalInvoiceItem o = new PayPalInvoiceItem();
		o.setName("Donation");
		o.setQuantity(1);
		o.setTotalPrice(new BigDecimal(5));

		invoice.add(o);

		payment.setInvoiceData(invoice);

		Intent paypalIntent = PayPal.getInstance().checkout(payment, getActivity(), this);
		getActivity().startActivityForResult(paypalIntent, 1); 
    }

	@Override
	public void onPaymentCanceled(String paymentStatus) {
		Log.i("fragments", "paypal cancelled: " + paymentStatus);
		//category, action, label, value
		GoogleAnalyticsTracker.getInstance().trackEvent("Paypal", "Canceled", paymentStatus, 0);
	}

	@Override
	public void onPaymentFailed(String paymentStatus, String correlationId, String payKey,
			String errorId, String errorMessage) {
		Log.i("fragments", "paypal failed: " + paymentStatus);
		//category, action, label, value
		GoogleAnalyticsTracker.getInstance().trackEvent("Paypal", "Failed", errorId + "|" + errorMessage, 0);
	}

	@Override
	public void onPaymentSucceeded(String transactionId, String paymentStatus) {

		Log.i("fragments", "paypal succeeded: " + paymentStatus);
		long price = ((long) 5 * 1000000);
		Transaction.Builder builder = new Builder(transactionId, price);
		builder.setStoreName("KianaByProxy");
		Transaction transaction = builder.build();
		
		GoogleAnalyticsTracker.getInstance().addTransaction(transaction);
		//category, action, label, value
		GoogleAnalyticsTracker.getInstance().trackEvent("Paypal", "Succeeded", paymentStatus, 0);
	}
	
	@Override
	public Path getPath() {
		return Path.DONATE;
	}
	
	public void trackException(String category, String message) {
		//category, action, label, value
		GoogleAnalyticsTracker.getInstance().trackEvent(category, "Exception", message, 0);
	}
}
