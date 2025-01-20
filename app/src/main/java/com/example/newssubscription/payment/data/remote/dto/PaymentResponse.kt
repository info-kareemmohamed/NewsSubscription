import com.google.gson.annotations.SerializedName

data class PaymentResponse(
    val payment_keys: List<PaymentKeys>,
    val id: String,
    val intention_detail: IntentionDetail,
    val client_secret: String,
    val payment_methods: List<PaymentMethod>,
    val special_reference: String?,
    val extras: Map<String, Any>,
    val confirmed: Boolean,
    val status: String,
    val created: String,
    val card_detail: String?,
    val card_tokens: List<String>,
    @SerializedName("object")
    val type: String
)


data class PaymentKeys(
    val integration: Int,
    val key: String,
    val gateway_type: String,
    val iframe_id: String?
)

data class IntentionDetail(
    val amount: Int,
    val currency: String,
    val items: List<Item>,
    val billing_data: BillingData
)

data class PaymentMethod(
    val integration_id: Int,
    val alias: String?,
    val name: String?,
    val method_type: String,
    val currency: String,
    val live: Boolean,
    val use_cvc_with_moto: Boolean
)

data class Item(
    val name: String,
    val amount: Float,
    val description: String,
    val quantity: Int
)