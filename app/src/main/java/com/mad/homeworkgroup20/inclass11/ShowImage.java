package com.mad.homeworkgroup20.inclass11;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kc.unsplash.Unsplash;
import com.kc.unsplash.models.Photo;
import com.kc.unsplash.models.SearchResults;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ShowImage extends AppCompatActivity {
    private static final String Client_ID = "2d4588f3a189ff603b2e7579139e1732ac2435da3f6063430920a2aadb7ef70a";
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    public  List<Photo> photos ;
    public void reset(){
        
        mViewPager.setAdapter(null);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

      final  Unsplash unsplash = new Unsplash(Client_ID);

        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (photos!= null)  {photos.clear();
                reset();
                    Log.d("test", "onClick: "+photos.size());}

                TextView search = findViewById(R.id.search);

                String query = search.getText().toString();
                if (query.isEmpty())
                    query="tower";

                unsplash.searchPhotos(query,null,50,new Unsplash.OnSearchCompleteListener() {

                    @Override
                    public void onComplete(SearchResults results) {
                        Log.d("Photos", "Total Results Found " + results.getTotal());
                        photos = results.getResults();

                        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
                        mViewPager = (ViewPager) findViewById(R.id.container);
                        mViewPager.setAdapter(mSectionsPagerAdapter);
                     /*   mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
                        mViewPager = (ViewPager) findViewById(R.id.container);
                        mViewPager.setAdapter(mSectionsPagerAdapter);*/
                    }

                    @Override
                    public void onError(String error) {
                        Log.d("Unsplash", error);
                    }
                });
            }
        });

        // Set up the ViewPager with the sections adapter.




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            if (photos!= null)  photos.clear();
            TextView tv = findViewById(R.id.search);
            tv.setText("");
            reset();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_URL = "photoURL";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber,String photourl) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString(ARG_URL, photourl);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_show_image, container, false);
           // TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            ImageView image = rootView.findViewById(R.id.imageView);
            if (getArguments().getString(ARG_URL)!="")
           Picasso.get().load(getArguments().getString(ARG_URL)).into(image);
          //  textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (photos!=null && photos.size()>0) {
                Photo photo = photos.get(position);
                return PlaceholderFragment.newInstance(position + 1, photo.getUrls().getRegular());
            }
            return  PlaceholderFragment.newInstance(position + 1,"");
        }

        @Override
        public void startUpdate(ViewGroup container) {
            super.startUpdate(container);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            int ret=0;
            if (photos!= null)
                ret=photos.size();
           return ret;
        }
    }
}
