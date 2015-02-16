# GridImageSearch

This is an Android application for a Google image search, which allows a user to select search filters and paginate results infinitely, using the [Google Image Search API](https://developers.google.com/image-search/v1/jsondevguide#json_reference) 

Time Spent: ~20 - 22 hours 
Completed user stories:

 * [x] Required: User can enter a search query that will display a grid of image results from the Google Image API.
 * [x] Required: User can click on "settings" which allows selection of advanced search options to filter results.
 * [x] Required: User can configure advanced search filters such as: Size, Color, Type, Site
 * [x] Required: Subsequent searches will have any filters applied to the search results (unless it's a new image query)
 * [x] Required: User can tap on any image in results to see the image full-screen 
 * [x] Required: User can [scroll down “infinitely”](http://guides.codepath.com/android/Endless-Scrolling-with-AdapterViews) to continue loading more image results (up to 8 pages). Also shows a loading bar in the last row. 
 * [x] Advanced: Robust error handling, [check if internet is available](http://guides.codepath.com/android/Sending-and-Managing-Network-Requests#checking-for-network-connectivity), handle error cases, network failures
 * [x] Advanced: Use the [ActionBar SearchView](http://guides.codepath.com/android/Extended-ActionBar-Guide#adding-searchview-to-actionbar) or custom layout as the query box instead of an EditText
 * [x] Advanced: User can [share an image](http://guides.codepath.com/android/Sharing-Content-with-Intents) to their friends or email it to themselves
 * [x] Advanced: Replace Filter Settings Activity with a lightweight [modal overlay](http://guides.codepath.com/android/Using-DialogFragment) 
 * [x] Advanced: Improve the user interface and experiment with image assets and/or styling and coloring. Attempted this as follows:
       - Opaque gears background on settings/advanced filters dialog
       - Image Detail Avticity shows an opaque full size version of the image being shown, in the background. 
       - Placeholders for images while they're loading. 
       - Attempted to maintain the aspect ratio of the image when it is being shown in full screen (if its original width and/or height are greater than screen width/height) 
 * [x] Bonus: Use the [StaggeredGridView](https://github.com/f-barth/AndroidStaggeredGrid) to display improve the grid of image results
 * [x] Bonus: User can [zoom or pan images](https://github.com/MikeOrtiz/TouchImageView) displayed in full-screen detail view
 * Used Parcelable instead of Serializables for passing complex objects in bundles and intents 
 
Libraries that helped make the project easier :-)

[Android async-http](http://loopj.com/android-async-http/) to make calls to the Google API

[Picasso](http://square.github.io/picasso/) to help load the images

[StaggeredGridView](https://github.com/f-barth/AndroidStaggeredGrid) to make the GridView cleaner

Video Walk Through: 

![VideoWalkThrough](VideoWalkThroughForGridImgSearch.gif)

Wasn't able to show Sharing via the emulator. So showing the below gif from my phone: 
![Sharing](ImageSharing.gif)


