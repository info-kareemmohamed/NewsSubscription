data class PaymentRequest(
    val amount: Float,
    val currency: String,
    val payment_methods: List<Int>,
    val billing_data: BillingData,
    val customer: Customer,
    val extras: Map<String, String>
)

data class BillingData(
    val apartment: String="",
    val first_name: String,
    val last_name: String,
    val street: String="",
    val building: String="",
    val phone_number: String="0",
    val city: String="",
    val country: String="",
    val email: String,
    val floor: String="",
    val state: String=""
)

data class Customer(
    val first_name: String,
    val last_name: String,
    val email: String,
)
