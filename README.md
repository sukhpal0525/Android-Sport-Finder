# android-app
CS3MDD Coursework

       Setup Instructions:

1. Import in Android Studio
- Unzip the .zip file.
- Import "android-app" in Android Studio.

2. Set local SDK
- File -> Project Structure -> SDK Location
- Set local.properties.
- Set "Compile SDK Version" to 34.

3. Run app
- Click "Edit Configurations"
  - Click "Add New Configuration"
    - Select "Android App"
      - Select SportsFinder.app.main
- Apply and OK
- Run

4. (Optional) Edit virtual device's location (for "Live Location" feature)
- Change device's location in "Extended Controls" to the UK.
(The default device location is San Francisco, USA)

            Notes:

CAN'T SEE THE GOOGLE MAP?	<------
(This is an emulator graphics rendering issue when using an emulator)
(I even installed the SDK and an emulator via commandline-tools and it still has the same issue)
(However, I've run my app from my phone countless times and I've never had this map issue).
- Wipe Data from the virtual device.
- Ensure you're using a virtual device with Play Store enabled.

Unable to see weather data?
For some reason, weather data isn't visible on my desktop, but it is on my laptop.

If it isnt visible for you please do the following:
- Wipe data from the virtual device.
- Re-run the app.

Want to reset all app data?
- Uninstall the app.