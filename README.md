XKCD Demo
================

This Android app is a small demo app for viewing XKCD comics.

Features included:
- View current XKCD
- Browse (1 to 2100+)
- Favorites (are also saved to be available in offline modus)
- Search
- Daily Notification on new XKCDs.


Library used:
- AndroidX / Jetpack
- Retrofit2
- PhotoView
- Glide
- Gson

The app consists of only 1 activity (MainActivity) and several Fragments.

The most important Fragments are the GalleryFragment and the ItemFragment.

The GalleryFragment contains av ViewPager which let the user swipe through sets of Items.

Depending on what menu option which is choosen (Current, Favorites, Browse, or Search), the
list is filled by using Retrofit and calls to XKCD remote API. Favorites are stored in a local (Room)
database. Images are loaded (and cached) using the Glide library. To support Zoom, Pan and
animated gifs, the app dependent on PhotoView library.

The GalleryFragment and ItemFragment are using their own respective ViewModels to separate
network-loading and state-saving logic from ui-code.

ViewModels are using the Repository to load data from remote and local source (XKCD api and Room DB)





---

Vidar Vestnes
 
05. Aug. 2019
