XKCD Demo
================

This Android app is a small demo app for viewing XKCD comics using the free and opensource XKCD API.

Features included:
- View current XKCD
- Browse (1 to 2100+)
- Favorites (with offline support)
- Search
- Daily Notification on new XKCDs.


Library used:
- AndroidX / Jetpack
- Retrofit2
- PhotoView
- Glide
- Gson

The app is built upon 1 activity (MainActivity) and several Fragments.

The most important Fragments are GalleryFragment and ItemFragment.

The GalleryFragment contains av ViewPager which enables the user to swipe through sets of Items.

The user can change item set via the menu (Current, Favorites, Browse, or Search via the Titlebar). The
lists are powered by using Retrofit and fetches data from XKCD Webservers. Favorited items are stored in the local (Room)
database. Images are loaded (and cached) using the Glide library and displayed in a PhotoView widget (supporting Zoom, Pan and
animated gifs).

The GalleryFragment and ItemFragment are using ViewModels to separate save its view-state and do network-loading.

ViewModels are again dependant on Repository, which again is dependant on Room and Retrofit. (See Architectual image at bottom).





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
 
<br/>

Architecture
<img src="https://user-images.githubusercontent.com/256259/62431492-378be280-b728-11e9-89eb-6d863f945742.png" />


Proxy: (not fully implemented)  https://github.com/vvestnes/xkcd-demo-www-proxy

---

Vidar Vestnes
 
05. Aug. 2019
