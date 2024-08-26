# College Fix It

Welcome to the College Fix It !! 🎓🚀 This app is designed to streamline the complaint registration process for students and ensure that their concerns are addressed promptly. With a clean and intuitive interface, students can easily register their complaints, and guards or authorities can manage and respond to these complaints efficiently.

## Features

- **Student Registration**  
  Students can register using Firebase Authentication. Each student receives an email verification for account activation.

- **Guard Registration**  
  Guards are added by admin approval, ensuring proper authorization.

- **Complaint Registration**  
  Students can submit complaints with titles and detailed descriptions. These complaints are visible to guards for review.

- **Complaint Voting**  
  Students can upvote complaints they relate to, helping prioritize issues based on popularity.

- **Notification System**  
  Real-time notifications are sent to guards when a complaint is registered, ensuring quick responses.

- **Complaint Deletion**  
  Students can easily delete their complaints if needed.

- **Dynamic UI**  
  The app features a beautiful and responsive UI that adapts to various screen sizes.

- **Bottom Navigation**  
  Navigate seamlessly between Home, Add Complaint, and Profile sections.

## Tech Stack

- **Android**: For building the app.
- **Firebase Authentication**: User authentication and email verification.
- **Firestore**: Real-time database for storing and retrieving data.
- **Lottie**: For smooth and engaging animations.
- **RecyclerView**: Efficiently displaying a list of complaints.
- **Material 3**: Modern and attractive UI components.
- **ViewModel**: Managing UI-related data in a lifecycle-conscious way.
- **LiveData**: Observing changes in data and updating the UI.
- **Firebase Cloud Messaging**: Sending notifications to users.
- **Retrofit & OkHttp3**: Networking and API interactions.
- **Node.js Custom Server**: For sending notifications and handling specific server-side logic.

## Getting Started

### Prerequisites

- Android Studio
- Firebase Project
- Node.js Server (for custom notifications)

### Installation

1. **Clone the repository**

    ```bash
    git clone https://github.com/TanishMoral11/College-Fix-It.git
    ```

2. **Open the project in Android Studio**

3. **Set up Firebase**

    - Add your Firebase configuration files (`google-services.json` for Android).
    - Follow the Firebase setup instructions to enable Authentication, Firestore, and Cloud Messaging.

4. **Run the app**

    - Build and run the app on an Android device or emulator.

## Contributing

We welcome contributions to improve the app. Please follow these steps to contribute:

1. Fork the repository.
2. Create a new branch for your changes.
3. Make your changes and test thoroughly.
4. Submit a pull request with a clear description of your changes.


