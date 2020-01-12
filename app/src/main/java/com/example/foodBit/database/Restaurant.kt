package com.example.foodBit.database

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "restaurant")
data class Restaurant(
	@PrimaryKey(autoGenerate = false)
	@field:SerializedName("id")
	var resId: String = "0",

	@field:SerializedName("cityId")
	var cityId: Int = 0,

	@field:SerializedName("categories")
	var categories: String = "0",

	@field:SerializedName("include_bogo_offers")
	var includeBogoOffers: Boolean? = null,

	@field:SerializedName("has_online_delivery")
	var hasOnlineDelivery: Int? = null,

	@field:SerializedName("user_aggregate_rating")
	var userAggregateRating: String? = null,

	@Ignore
	@field:SerializedName("establishment_types")
	var establishmentTypes: List<Any>? = null,

	@field:SerializedName("has_table_booking")
	var hasTableBooking: Int? = null,

	@field:SerializedName("thumb")
	var thumb: String? = null,

	@field:SerializedName("average_cost_for_two")
	var averageCostForTwo: Int? = null,

	@field:SerializedName("menu_url")
	var menuUrl: String? = null,

	@field:SerializedName("price_range")
	var priceRange: Int? = null,

	@field:SerializedName("switch_to_order_menu")
	var switchToOrderMenu: Int? = null,

	@Ignore
	@field:SerializedName("photos")
	var photos: List<PhotosItem>? = null,

	@Ignore
	@field:SerializedName("R")
	var R: R? = null,

	@field:SerializedName("all_reviews_count")
	var allReviewsCount: Int? = null,

	@field:SerializedName("is_table_reservation_supported")
	var tableReservationSupported: Int? = null,

	@field:SerializedName("timings")
	var timings: String? = null,

	@field:SerializedName("currency")
	var currency: String? = null,

	@field:SerializedName("opentable_support")
	var opentableSupport: Int? = null,

	@Ignore
	@field:SerializedName("all_reviews")
	var allReviews: AllReviews? = null,

	@Ignore
	@field:SerializedName("user_rating")
	var userRating: UserRating? = null,

	@Ignore
	@field:SerializedName("offers")
	var offers: List<Any?>? = null,

	@field:SerializedName("apikey")
	var apikey: String? = null,

	@field:SerializedName("is_delivering_now")
	var deliveringNow: Int? = null,

	@field:SerializedName("deeplink")
	var deeplink: String? = null,

	@field:SerializedName("is_zomato_book_res")
	var zomatoBookRes: Int? = null,

	@Ignore
	@field:SerializedName("establishment")
	var establishment: List<String>? = null,

	@field:SerializedName("featured_image")
	var featuredImage: String? = null,

	@field:SerializedName("url")
	var url: String? = null,

	@field:SerializedName("cuisines")
	var cuisines: String? = null,

	@field:SerializedName("phone_numbers")
	var phoneNumbers: String? = null,

	@field:SerializedName("photo_count")
	var photoCount: Int? = null,

	@Ignore
	@field:SerializedName("highlights")
	var highlights: List<Any>? = null,

	@field:SerializedName("events_url")
	var eventsUrl: String? = null,

	@field:SerializedName("name")
	var name: String? = null,

	@Ignore
	@field:SerializedName("location")
	var location: Location? = null,

	@field:SerializedName("book_again_url")
	var bookAgainUrl: String? = null,

	@field:SerializedName("is_book_form_web_view")
	var bookFormWebView: Int? = null,

	@field:SerializedName("book_form_web_view_url")
	var bookFormWebViewUrl: String? = null,

	@field:SerializedName("mezzo_provider")
	var mezzoProvider: String? = null,

	@field:SerializedName("photos_url")
	var photosUrl: String? = null,

	@field:SerializedName("order_deeplink")
	var orderDeeplink: String? = null,

	@field:SerializedName("order_url")
	var orderUrl: String? = null
) {
	var indexInResponse = -1
}