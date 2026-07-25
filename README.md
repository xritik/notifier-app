# Notif Mirror — Building the APK via GitHub Actions

You don't need Android Studio installed. GitHub's own servers will compile
the app for you. Steps:

## 1. Create a GitHub repo
- Go to https://github.com/new
- Name it anything, e.g. `notif-mirror`
- Keep it **Public** or **Private**, either works
- Don't add a README/gitignore (keep it empty) — click "Create repository"

## 2. Push this project to it
Open a terminal inside this unzipped `NotifMirror` folder and run:

```bash
git init
git add .
git commit -m "Initial commit"
git branch -M main
git remote add origin https://github.com/<your-username>/notif-mirror.git
git push -u origin main
```

(Replace `<your-username>` with your actual GitHub username.)

## 3. Watch it build
- Go to your repo on github.com → click the **Actions** tab
- You'll see a workflow run called "Build APK" already running (it starts
  automatically on push)
- It takes about 3-5 minutes the first time

## 4. Download the APK
- Once the run finishes (green check ✅), click into it
- Scroll to the **Artifacts** section at the bottom
- Download `notif-mirror-apk` — it's a zip containing `app-debug.apk`

## 5. Install it on your phone
- Copy the `.apk` to your phone (email it to yourself, use Google Drive,
  or a USB cable — any method works)
- On your phone, tap the file to install it
- You'll likely see a warning like "install from unknown sources" —
  this is normal for any app not from the Play Store. Allow it just for
  this install.

## 6. Use the app
Same as before:
1. Open the app → tap "Grant Notification Access" → enable Notif Mirror
2. Tap "Start Server"
3. Turn on your phone's hotspot, connect your laptop to it
4. Open `notif-mirror-dashboard.html` on the laptop, enter the IP shown
   on the phone screen, click Connect

---

**No Git installed on your computer?** Get it from https://git-scm.com —
or, even simpler, use GitHub Desktop (https://desktop.github.com) which
lets you do steps 1-2 by dragging the folder in and clicking "Publish
repository," no command line needed.
