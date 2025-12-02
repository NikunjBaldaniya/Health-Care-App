[![Typing SVG](https://readme-typing-svg.herokuapp.com?font=Fira+Code&weight=700&size=27&pause=1000&color=22F700&width=550&lines=Health+Care+Android+Application)](https://git.io/typing-svg)

**Health Care** is an Android application designed for healthcare management, medicine search, and doctor consultation. It includes separate panels for users and admin, AI chat support, health news, medicine purchases, and location-based features.

## Technology Stack
- **Language:** Java/XML, Kotlin  
- **Build Tool:** Gradle (KTS)  
- **Database:** SQLite  
- **APIs Used:**  
  - [OpenRouter](https://openrouter.ai/) (AI Chat)  
  - [Newsdata.io](https://newsdata.io/) (Health Articles)  

## App Panels

### User Panel
- Search and buy medicines, add to cart  
- View medicine description, rating, and price  
- Interactive buy animation  
- Search and contact doctors via:  
  - Instagram (redirect)  
  - WhatsApp (redirect)  
  - Phone (dialer)  
  - Location (map redirect)  
- AI Chat support using the model: `mistralai/mistral-small-3.2-24b-instruct:free`  
- Health news and articles  
- SOS call (108 Ambulance)  
- Nearby hospitals and pharmacies (map redirect)  
- View Top Doctors and Top Medicines  
- Forgot password functionality with email verification (SMTP)  

### Admin Panel
- Manage doctors: Insert, Update, Delete, View  
- Manage medicines: Insert, Update, Delete, View  
- View total count of doctors, users, and medicines  

## Features
- Seamless navigation between user and admin panels  
- Real-time AI chat support  
- Map integration for hospitals and pharmacies  
- Contact integration for Instagram, WhatsApp, and phone  
- Health news and articles  
- Interactive UI with animations  

## Screenshots

<div style="display: flex; overflow-x: auto; gap: 10px; padding: 10px;">
    <img src="screenshorts/splash_screen.png" alt="splash_screen" width="250"/>
    <img src="screenshorts/onboardig_screen.png" alt="onboardig_screen" width="250"/>
    <img src="screenshorts/login_sign_up_screen.png" alt="login_sign_up_screen" width="250"/>
    <img src="screenshorts/sign_in.png" alt="sign_in" width="250"/>
    <img src="screenshorts/sign_up.png" alt="sign_up" width="250"/>
    <img src="screenshorts/create_new_password.png" alt="create_new_password" width="250"/>
    <img src="screenshorts/forgot_password_email_verification.png" alt="forgot_password_email_verification" width="250"/>
    <img src="screenshorts/otp_verification.png" alt="otp_verification" width="250"/>
    <img src="screenshorts/admin_create_doctor.png" alt="admin_create_doctor" width="250"/>
    <img src="screenshorts/doctor_details_1.png" alt="doctor_details_1" width="250"/>
    <img src="screenshorts/doctor_details_2.png" alt="doctor_details_2" width="250"/>
    <img src="screenshorts/doctor_search.png" alt="doctor_search" width="250"/>
    <img src="screenshorts/update_delete_doctor.png" alt="update_delete_doctor" width="250"/>
    <img src="screenshorts/user_doctor_activity.png" alt="user_doctor_activity" width="250"/>
    <img src="screenshorts/user_doctor_search.png" alt="user_doctor_search" width="250"/>
    <img src="screenshorts/top_doctor_activity.png" alt="top_doctor_activity" width="250"/>
    <img src="screenshorts/user_all_doctors.png" alt="user_all_doctors" width="250"/>
    <img src="screenshorts/admin_add_medicine.png" alt="Aadmin_add_medicine" width="250"/>
    <img src="screenshorts/admin_medicine_fragment.png" alt="admin_medicine_fragment" width="250"/>
    <img src="screenshorts/medicine_fragment.png" alt="medicine_fragment" width="250"/>
    <img src="screenshorts/medicine_detail_1.png" alt="medicine_detail_1" width="250"/>
    <img src="screenshorts/medicine_detail_2.png" alt="medicine_detail_2" width="250"/>
    <img src="screenshorts/update_delete_medicine.png" alt="update_delete_medicine" width="250"/>
    <img src="screenshorts/top_medicines.png" alt="top_medicines" width="250"/>
    <img src="screenshorts/admin_order_detail.png" alt="admin_order_detail" width="250"/>
    <img src="screenshorts/admin_order_fragment.png" alt="admin_order_fragment" width="250"/>
    <img src="screenshorts/cart_activity.png" alt="cart_activity" width="250"/>
    <img src="screenshorts/order_confirm.png" alt="order_confirm" width="250"/>
    <img src="screenshorts/user_order_activity.png" alt="user_order_activity" width="250"/>
    <img src="screenshorts/admin_new_user.png" alt="admin_new_user" width="250"/>
    <img src="screenshorts/admin_update_user.png" alt="admin_update_user" width="250"/>
    <img src="screenshorts/profile_fragment.png" alt="profile_fragment" width="250"/>
    <img src="screenshorts/user_dashboard.png" alt="user_dashboard" width="250"/>
    <img src="screenshorts/user_health.png" alt="user_health" width="250"/>
    <img src="screenshorts/height_weight_address.png" alt="height_weight_address" width="250"/>
    <img src="screenshorts/age_bloodgroop.png" alt="age_bloodgroop" width="250"/>
    <img src="screenshorts/select_gender.png" alt="select_gender" width="250"/>
    <img src="screenshorts/select_activity_level.png" alt="select_activity_level" width="250"/>
    <img src="screenshorts/settings_activity.png" alt="settings_activity" width="250"/>
    <img src="screenshorts/terms_activity.png" alt="terms_activity" width="250"/>
    <img src="screenshorts/ai_chat_activity.png" alt="ai_chat_activity" width="250"/>
    <img src="screenshorts/ai_chat_with_responce.png" alt="ai_chat_with_responce" width="250"/>
    <img src="screenshorts/admin_doctor_fragment.png" alt="admin_doctor_fragment" width="250"/>
    <img src="screenshorts/admin_home_fragment.png" alt="admin_home_fragment" width="250"/>
    <img src="screenshorts/health_fragment.png" alt="health_fragment" width="250"/>
</div>

## If You Want to Test App, Download APK & Run on Mobile  
(It Has Security So Don't Worry 😉)  

<a href="app/release/health-care.apk?raw=true">
  <img src="https://img.shields.io/badge/Download-APK-green?style=for-the-badge&logo=android" />
</a>

## How to Setup

1. Clone the repository:  
   ```bash
   git clone https://github.com/teams-thunder/nikunjApp.git
