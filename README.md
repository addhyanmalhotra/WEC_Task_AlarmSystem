# READMe
the Project is an alarm system, that sends an SMS containing a link to the last known location
of the user upon clicking the sos button
## Technologies used
[x] Backend Auth/Cloud Storage of user data(emergency contact) Firebase Auth and Firebase realtime Database
[x] SMS , default sms manager api
[x] Local persistent storage, Android Shared preferences (SQLlite seemed like over kill for this)
[x] Logos/Backgrounds designed in GIMP 2.10
[x] FusedLocationClient (google play services) for accurate last known location
[x] Vectors designed in inkscape (not yet implemented)
## Future Plans
[ ] implement maps APi to show user directions and contact of emergency services like closest Hospitals,Police stations FireSattions etc
[ ] Add dedicated layout files for Tablets and smaller screens
[ ] implement an in app password reset, instead of default firebase one
[ ] Implement whatsapp/messenger API to send alerts via them as well
[ ] Create Reciever end web app that shows live location as updated on firebase(instead of onetime static location sent now)

## Known Bugs
[x] Using Multiple contacts occsionally throws an error due incorrect formatting of phone number for older android versions,thus multiple contact has temporarily been disabled message goes only to primary contact
[x] UI not ver clean on non standard devices (ie tablets laptops older phones), PNG files need to be converted to vectors, will be ddone in due course
