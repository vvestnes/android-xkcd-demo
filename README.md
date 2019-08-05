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

<table>
 <tr>
  <td>
   Splash<br/>
   <img src="https://user-images.githubusercontent.com/256259/62431337-07900f80-b727-11e9-874e-0295270e7cba.png" width="100"/>
  </td>
  
  <td>
    Menu<br/>
   <img src="https://user-images.githubusercontent.com/256259/62431192-71a7b500-b725-11e9-9416-a0cedca7a814.png" width="100"/>
  </td>
  <td>
    Current<br/>
   <img src="https://user-images.githubusercontent.com/256259/62431187-62c10280-b725-11e9-907a-c5eb9825269c.png" width="100"/>
  </td>
  <td>
   Transcript<br/>
   <img src="https://user-images.githubusercontent.com/256259/62431334-052db580-b727-11e9-98d4-2017e883b85c.png" width="100"/>
  </td>
  
  <td>
   Share</br>
   <img src="https://user-images.githubusercontent.com/256259/62431330-fe06a780-b726-11e9-9048-9accd27f874c.png" width="100"/>
  </td>

  <td>
    Settings<br/>
   <img src="https://user-images.githubusercontent.com/256259/62431189-694f7a00-b725-11e9-8fc4-51214e20671b.png" width="100"/>
  </td>
  <td>
   Alarm<br/>
   <img src="https://user-images.githubusercontent.com/256259/62431332-0232c500-b727-11e9-9e0c-d0a0760b5096.png" width="100"/>
  </td>



 </tr>
 </table>



---

Vidar Vestnes
 
05. Aug. 2019
