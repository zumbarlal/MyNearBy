package com.ys.mynearby;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.ys.interfaces.IURLFetcher;
import com.ys.interfaces.WSFetchListener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by ss on 007-07-12-2016.
 */

public class URLFetcher implements IURLFetcher {
    ExecutorService executorService = null;
    private static URLFetcher urlFetcher = new URLFetcher();
    List<Future> runnable_future;

    private URLFetcher(){
        executorService = Executors.newSingleThreadExecutor();
        runnable_future = new ArrayList<>();
    }



    public static URLFetcher getURLFetcher(){
        return urlFetcher;
    }


    public String buildURL(Context context,PlaceLocation placeLocation,String keyword){
        Uri.Builder  builder = Uri.parse("https://maps.googleapis.com/maps/api/place/nearbysearch/json?").buildUpon();
        //new Uri.Builder();
        builder.appendQueryParameter("key", context.getString(R.string.google_places_ws_key));
        builder.appendQueryParameter("location", placeLocation.lat+","+placeLocation.lng);
        builder.appendQueryParameter("rankby", "distance" ); //keyword, name, or type
//        builder.appendQueryParameter("radius", "1000" ); //Note that radius must not be included if rankby=distance
        builder.appendQueryParameter("keyword", ""+keyword );
        builder.appendQueryParameter("language", "en" );
//        builder.appendQueryParameter("type", );
//        builder.appendQueryParameter("pagetoken", );
//        builder.appendQueryParameter("", );
//        builder.appendQueryParameter("", );

        Log.d("MNB",builder.build().toString());

        return  builder.build().toString();

    }

    public void startFetchURLBackground(final String url, final WSFetchListener wsFetchListener){

        Future future = executorService.submit(new Runnable() {
            @Override
            public void run() {
                String responceData = getURLResponse(url);
                wsFetchListener.onWSResponse(responceData);

            }
        });
        runnable_future.add(future);


//        AsyncTask asyncTask = new AsyncTask() {
//            @Override
//            protected Object doInBackground(Object[] objects) {
//
////                publishProgress(responceData);
//                return null;
//            }
//
//            @Override
//            protected void onProgressUpdate(Object... data) {
//                super.onProgressUpdate(data);
//                wsFetchListener.onWSResponse(String.valueOf(data[0]));
//            }
//        };
//        asyncTask.execute();
    }

    public synchronized String getURLResponse(String url){

        String responceData = null;

        try{
            URL url1 = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url1.openConnection();
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoInput(true);

            httpURLConnection.connect();

            int response = httpURLConnection.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK){
                InputStream inputStream = httpURLConnection.getInputStream();

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder stringBuilder = new StringBuilder();

                String line;
                while ((line = bufferedReader.readLine())!=null){
                    stringBuilder.append(line);
                }

                responceData = stringBuilder.toString();
            }

        }catch (Exception e){

        }

        return responceData;
    }

    @Override
    public void removeAllTask() {
        if (runnable_future.size()>0){
            Iterator<Future> futureIterator = runnable_future.iterator();
            while (futureIterator.hasNext()){
                Future future = futureIterator.next();
                future.cancel(true);
                futureIterator.remove();
            }
            runnable_future.clear();
        }
    }

}

/*
Languages supported

Language Code 	Language 	Language Code 	Language
ar 	Arabic 	kn 	Kannada
bg 	Bulgarian 	ko 	Korean
bn 	Bengali 	lt 	Lithuanian
ca 	Catalan 	lv 	Latvian
cs 	Czech 	ml 	Malayalam
da 	Danish 	mr 	Marathi
de 	German 	nl 	Dutch
el 	Greek 	no 	Norwegian
en 	English 	pl 	Polish
en-AU 	English (Australian) 	pt 	Portuguese
en-GB 	English (Great Britain) 	pt-BR 	Portuguese (Brazil)
es 	Spanish 	pt-PT 	Portuguese (Portugal)
eu 	Basque 	ro 	Romanian
eu 	Basque 	ru 	Russian
fa 	Farsi 	sk 	Slovak
fi 	Finnish 	sl 	Slovenian
fil 	Filipino 	sr 	Serbian
fr 	French 	sv 	Swedish
gl 	Galician 	ta 	Tamil
gu 	Gujarati 	te 	Telugu
hi 	Hindi 	th 	Thai
hr 	Croatian 	tl 	Tagalog
hu 	Hungarian 	tr 	Turkish
id 	Indonesian 	uk 	Ukrainian
it 	Italian 	vi 	Vietnamese
iw 	Hebrew 	zh-CN 	Chinese (Simplified)
ja 	Japanese 	zh-TW 	Chinese (Traditional)
 */


/*
types supported

    accounting
    airport
    amusement_park
    aquarium
    art_gallery
    atm
    bakery
    bank
    bar
    beauty_salon
    bicycle_store
    book_store
    bowling_alley
    bus_station
    cafe
    campground
    car_dealer
    car_rental
    car_repair
    car_wash
    casino
    cemetery
    church
    city_hall
    clothing_store
    convenience_store
    courthouse
    dentist
    department_store
    doctor
    electrician
    electronics_store
    embassy
    establishment (deprecated)
    finance (deprecated)
    fire_station
    florist
    food (deprecated)
    funeral_home
    furniture_store
    gas_station
    general_contractor (deprecated)
    grocery_or_supermarket (deprecated)
    gym
    hair_care
    hardware_store
    health (deprecated)
    hindu_temple
    home_goods_store
    hospital
    insurance_agency
    jewelry_store
    laundry
    lawyer
    library
    liquor_store
    local_government_office
    locksmith
    lodging
    meal_delivery
    meal_takeaway
    mosque
    movie_rental
    movie_theater
    moving_company
    museum
    night_club
    painter
    park
    parking
    pet_store
    pharmacy
    physiotherapist
    place_of_worship (deprecated)
    plumber
    police
    post_office
    real_estate_agency
    restaurant
    roofing_contractor
    rv_park
    school
    shoe_store
    shopping_mall
    spa
    stadium
    storage
    store
    subway_station
    synagogue
    taxi_stand
    train_station
    transit_station
    travel_agency
    university
    veterinary_care
    zoo
 */

/*
    addtional types

    administrative_area_level_1
    administrative_area_level_2
    administrative_area_level_3
    administrative_area_level_4
    administrative_area_level_5
    colloquial_area
    country
    establishment
    finance
    floor
    food
    general_contractor
    geocode
    health
    intersection
    locality
    natural_feature
    neighborhood
    place_of_worship
    political
    point_of_interest
    post_box
    postal_code
    postal_code_prefix
    postal_code_suffix
    postal_town
    premise
    room
    route
    street_address
    street_number
    sublocality
    sublocality_level_4
    sublocality_level_5
    sublocality_level_3
    sublocality_level_2
    sublocality_level_1
    subpremise

 */