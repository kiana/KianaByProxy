package com.kt.kbp.paypal;

import java.io.Serializable;
import java.math.BigDecimal;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.apps.analytics.Transaction;
import com.google.android.apps.analytics.Transaction.Builder;
import com.kt.kbp.MainActivity;
import com.kt.kbp.R;
import com.kt.kbp.activitypath.ActivityPath;
import com.kt.kbp.activitypath.ActivityPathInterface;
import com.kt.kbp.common.Constants;
import com.kt.kbp.googleanalytics.GoogleAnalyticsActivity;
import com.paypal.android.MEP.CheckoutButton;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalActivity;
import com.paypal.android.MEP.PayPalInvoiceData;
import com.paypal.android.MEP.PayPalInvoiceItem;
import com.paypal.android.MEP.PayPalPayment;
import com.paypal.android.MEP.PayPalResultDelegate;

public class DonateActivity extends GoogleAnalyticsActivity implements PayPalResultDelegate, ActivityPathInterface, Serializable {

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
				i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				startActivity(i);
			}
		});
		
        PayPal payPalObject = PayPal.initWithAppID(this, Constants.PAYPAL_LIVE, PayPal.ENV_LIVE);
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
    	trackEvent("Paypal", "PayPalButton", "Click", 0);
		
		PayPalPayment payment = new PayPalPayment();

		payment.setCurrencyType("USD");
		payment.setRecipient("kianabyproxy@gmail.com");
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
		startActivityForResult(paypalIntent, 1); 
    }

	/*
	 * (non-Javadoc)
	 * @see com.paypal.android.MEP.PayPalResultDelegate#onPaymentCanceled(java.lang.String)
	 */
	@Override
	public void onPaymentCanceled(String paymentStatus) {
		//category, action, label, value
		trackEvent("Paypal", "Canceled", paymentStatus, 0);
	}

	/*
	 * (non-Javadoc)
	 * @see com.paypal.android.MEP.PayPalResultDelegate#onPaymentFailed(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void onPaymentFailed(String paymentStatus, String correlationId, String payKey,
			String errorId, String errorMessage) {
		//category, action, label, value
		trackEvent("Paypal", "Failed", errorId + "|" + errorMessage, 0);
	}

	/*
	 * (non-Javadoc)
	 * @see com.paypal.android.MEP.PayPalResultDelegate#onPaymentSucceeded(java.lang.String, java.lang.String)
	 */
	@Override
	public void onPaymentSucceeded(String transactionId, String paymentStatus) {
		
		long price = ((long) 5 * 1000000);
		Transaction.Builder builder = new Builder(transactionId, price);
		builder.setStoreName("KianaByProxy");
		Transaction transaction = builder.build();
		
		trackTransaction(transaction);
		//category, action, label, value
		trackEvent("Paypal", "Succeeded", paymentStatus, 0);
	}

	@Override
	public ActivityPath getActivityPath() {
		return ActivityPath.DONATE;
	}
	
}

