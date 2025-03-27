# 🚀 Contributors Guide
Thank you for your interest in contributing to **College-Fix-It**! We appreciate your efforts to improve this project. Follow the steps below to set up the project and start contributing.
---
## 🔗 Forking and Cloning the Repository
### 1️⃣ **Fork the Repository**
- Visit the [College-Fix-It Repository](https://github.com/iiitl/College-Fix-It).
- Click on the Fork button (top-right corner).
### 2️⃣ **Clone Your Forked Repository**
- Open **Android Studio**.
- Navigate to: **`File > New > Project from Version Control > Git`**
- Paste the URL of your forked repository:
  ```bash
  https://github.com/YOUR_USERNAME/College-Fix-It.git
  ```
- Click `Clone`.
## ▶️ Running the App
1. Sync your project with **Gradle Files**.
2. Build and run the app on your **emulator or physical device**.
## ❌ Troubleshooting Issues
If you face issues while running the app, 
<details>
<summary>Click to view error screenshot</summary>

![WhatsApp Image 2025-03-27 at 22 14 00_0f1eaccf](https://github.com/user-attachments/assets/c3921304-77c0-4018-ad22-ba74a591f732)
</details>

 follow these steps:
### ✅ Set Gradle JDK
1. **Go to:** `Settings > Build, Execution, Deployment > Build Tools > Gradle`
2. **Set `Gradle JDK` to: `JDK 17 (corretto-17)`**.
<details>
<summary>Click to view Gradle JDK configuration screenshot</summary>

![image](https://github.com/user-attachments/assets/5847eb14-beb3-4d97-aca2-cef11dc379bb)
</details>

3. **Enable External Annotations:** Check the box for *Download external annotations for dependencies*.
4. Click `Apply` and `OK`.
**⏳ This may take 7-10 minutes**. In the meantime, take a break and touch some grass. 🌿😌 If the issue persists, contact an **App Wing Member**.
---
## 🕵️ **Discovering and Reporting Issues**
Contributing isn't just about solving existing problems—it's also about identifying new opportunities for improvement! If you notice any:
- Bugs in the app
- Areas for performance optimization
- Missing features
- Usability improvements
- Documentation gaps

**Filing an issue is a fantastic way to show your genuine interest and commitment to the project!** 🌟 When you spot something that could be enhanced, don't hesitate to:
- Create a detailed issue on our GitHub repository
- Describe the problem clearly
- Suggest potential solutions if possible
- Include steps to reproduce (if it's a bug)

Remember, every issue you file is a valuable contribution that helps make **College-Fix-It** better for everyone. Your keen eye and proactive approach are what drive open-source projects forward! 🚀🔍


## 🛠️ Contribution Workflow
### 1️⃣ **Get the Issue Assigned**
- Before working on an issue, **comment** on it and ask for assignment.
### 2️⃣ **Create a New Branch**
- Once assigned, create a new branch for your work:
  ```bash
  git checkout -b issue-<issue-number>
  ```
### 3️⃣ **Solve the Issue & Commit Changes**
- Make your changes and commit them with a descriptive message:
  ```bash
  git commit -m "Fixes issue #<issue-number>: <description of fix>"
  ```
### 4️⃣ **Push Your Changes**
- Push the branch to your forked repository:
  ```bash
  git push origin issue-<issue-number>
  ```
### 5️⃣ **Create a Pull Request**
- Go to the original repository.
- Click on `Pull Requests` → `New Pull Request`.
- Select your branch and provide a detailed description.
- Submit the pull request!
## 🎉 Thanks for Contributing!
We appreciate your efforts and look forward to working with you! 🚀✨ Happy Coding! 💻❤️
