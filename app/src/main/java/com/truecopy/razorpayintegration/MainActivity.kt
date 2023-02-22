package com.truecopy.razorpayintegration

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultListener
import com.razorpay.PaymentResultWithDataListener
import org.json.JSONObject
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity(), /*PaymentResultListener,*/ PaymentResultWithDataListener {

    private lateinit var amountEdt: EditText
    private lateinit var payBtn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        amountEdt = findViewById(R.id.idEdtAmount)
        payBtn = findViewById(R.id.idBtnPay)

//        payBtn.setOnClickListener{
//            startPayment()
//        }


        val co = Checkout()
        co.setKeyID("rzp_test_9NywE6dEO3fTkm")

        payBtn.setOnClickListener{
            val sAmount =  amountEdt.text.toString()
            val amount = (sAmount.toFloat() * 100).roundToInt()
            val checkout = Checkout()
            checkout.setKeyID("rzp_test_9NywE6dEO3fTkm")

            val jsonObject = JSONObject()
            try{
                jsonObject.put("name", "Truecopy Credentials")
                jsonObject.put("description", "Test payment")
                jsonObject.put("theme.color", "#F95A2C")
                jsonObject.put("currency", "INR")
                jsonObject.put("amount", amount)
//                val retryObj = JSONObject();
//                retryObj.put("enabled", true);
//                retryObj.put("max_count", 4);
//                jsonObject.put("retry", retryObj);

//                val prefill = JSONObject()
//                prefill.put("email","someshswami9@gmail.com")
//                prefill.put("contact","9370687891")
//                jsonObject.put("prefill", prefill)

            } catch (e:Exception){
                e.printStackTrace()
            }

            checkout.open(this@MainActivity,jsonObject)
        }

    }
    override fun onPaymentError(errorCode: Int, response: String?, p2: PaymentData?) {
        Log.i("PAYMET EEROR", response + errorCode.toString())
        Toast.makeText(this, "Payment is Not successfully", Toast.LENGTH_SHORT).show()
        if (p2 != null) {
            Log.i("PAYMENT NOT SUCCESS Data", p2.data.toString())
        }
        Checkout.clearUserData(this@MainActivity)
    }
    override fun onPaymentSuccess(razorpayPaymentId: String?, paymentData: PaymentData) {
        if (razorpayPaymentId != null) {
            Log.i("PAYMENT SUCCESS", razorpayPaymentId)
            Log.i("PAYMENT SUCCESS Data", paymentData.data.toString())
            Checkout.clearUserData(this@MainActivity)
        }
        val paymentDetails = JSONObject()
        paymentDetails.put("payment_data",paymentData.data)
        paymentDetails.put("payment_order_id",paymentData.orderId)
        paymentDetails.put("payment_wallet",paymentData.externalWallet)
        paymentDetails.put("payment_Contact",paymentData.userContact)
        paymentDetails.put("payment_Email",paymentData.userEmail)
        paymentDetails.put("payment_Sign",paymentData.signature)

        Log.i("PAYMENT SUCCESS Data with details", paymentDetails.toString())

        Toast.makeText(this, "Payment is successful : $razorpayPaymentId", Toast.LENGTH_SHORT).show()
    }

}
