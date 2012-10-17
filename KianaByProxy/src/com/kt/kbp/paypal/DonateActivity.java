package com.kt.kbp.paypal;

import java.io.Serializable;
import java.math.BigDecimal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Transaction;
import com.kt.kbp.MainActivity;
import com.kt.kbp.R;
import com.kt.kbp.common.Constants;
import com.paypal.android.MEP.CheckoutButton;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalActivity;
import com.paypal.android.MEP.PayPalInvoiceData;
import com.paypal.android.MEP.PayPalInvoiceItem;
import com.paypal.android.MEP.PayPalPayment;
import com.paypal.android.MEP.PayPalResultDelegate;

public class DonateActivity extends Activity implements PayPalResultDelegate, Serializable {

	private static final long serialVersionUID = 4607082035320065280L;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal);

		TextView back = (TextView) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(), MainActivity.class);
				startActivity(i);
			}
		});
		
        PayPal payPalObject = PayPal.initWithAppID(this, Constants.PAYPAL_SANDBOX, PayPal.ENV_SANDBOX);
        getCheckoutButton(payPalObject);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_paypal, menu);
        return true;
    }

    public void getCheckoutButton(PayPal payPalObject) {
    	CheckoutButton launchPayPalButton = payPalObject.getCheckoutButton(this, PayPal.BUTTON_278x43, CheckoutButton.TEXT_DONATE);
    	launchPayPalButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				payPalButtonClick();
			}
		});
    	setContentView(R.layout.activity_paypal);
    	((RelativeLayout)findViewById(R.id.donate_button)).addView(launchPayPalButton);
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch(resultCode) {
    	case PayPalActivity.RESULT_OK:
	    	//String payKey = data.getStringExtra(PayPalActivity.EXTRA_PAY_KEY);
  	      	startActivity(new Intent(DonateActivity.this, SucceededActivity.class));
	    	break;
    	case PayPalActivity.RESULT_CANCELED:
  	      	startActivity(new Intent(DonateActivity.this, CancelledActivity.class));
	    	break;
    	case PayPalActivity.RESULT_FAILURE:
	    	//String errorID = data.getStringExtra(PayPalActivity.EXTRA_ERROR_ID);
	    	//String errorMessage = data.getStringExtra(PayPalActivity.EXTRA_ERROR_MESSAGE);
  	      	startActivity(new Intent(DonateActivity.this, FailedActivity.class));
    	}
    }
    
    public void payPalButtonClick() {
    	  PayPalPayment payment = new PayPalPayment();

    	  payment.setCurrencyType("USD");
    	  payment.setRecipient("kiana._1349411308_biz@gmail.com");
    	  payment.setSubtotal(new BigDecimal(5));

    	  payment.setPaymentType(PayPal.PAYMENT_TYPE_NONE);

    	  PayPalInvoiceData invoice = new PayPalInvoiceData();

    	  PayPalInvoiceItem o = new PayPalInvoiceItem();
    	  o.setName("Donation");
    	  o.setQuantity(1);
    	  o.setTotalPrice(new BigDecimal(5));
    	  
    	  invoice.add(o);
    	  
    	  payment.setInvoiceData(invoice);

    	  Intent paypalIntent = PayPal.getInstance().checkout(payment, this, this);
    	  startActivityForResult(paypalIntent, 1); //TODO returned result probably contains orderId and other info needed to trackTransaction
    }

	/*
	 * (non-Javadoc)
	 * @see com.paypal.android.MEP.PayPalResultDelegate#onPaymentCanceled(java.lang.String)
	 */
	@Override
	public void onPaymentCanceled(String paymentStatus) {
		//TODO WEB ANALYTICS -- trackEvent
	}

	/*
	 * (non-Javadoc)
	 * @see com.paypal.android.MEP.PayPalResultDelegate#onPaymentFailed(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void onPaymentFailed(String paymentStatus, String correlationId, String payKey,
			String errorId, String errorMessage) {
		//TODO WEB ANALYTICS -- trackEvent
	}

	/*
	 * (non-Javadoc)
	 * @see com.paypal.android.MEP.PayPalResultDelegate#onPaymentSucceeded(java.lang.String, java.lang.String)
	 */
	@Override
	public void onPaymentSucceeded(String payKey, String paymentStatus) {
		//TODO WEB ANALYTICS -- trackTransaction, trackEvent
	}
	
    @Override
    public void onStart() {
    	super.onStart();
    	EasyTracker.getInstance().activityStart(this);
    }
    
    
    @Override
    public void onStop() {
    	super.onStop();
    	EasyTracker.getInstance().activityStop(this);
    }
    
    public void trackTransaction(Transaction transaction) {
    	EasyTracker.getInstance().setContext(this);	
    	
    	//Transaction transaction = new Transaction();
    	
    	EasyTracker.getTracker().trackTransaction(transaction);
    }
}
