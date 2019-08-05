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



![Screenshot_1564956247](https://user-images.githubusercontent.com/256259/62431187-62c10280-b725-11e9-907a-c5eb9825269c.png)
![Screenshot_1564956264](https://user-images.githubusercontent.com/256259/62431189-694f7a00-b725-11e9-8fc4-51214e20671b.png)
![Screenshot_1564956240](https://user-images.githubusercontent.com/256259/62431192-71a7b500-b725-11e9-9416-a0cedca7a814.png)



---

Vidar Vestnes
 
05. Aug. 2019
