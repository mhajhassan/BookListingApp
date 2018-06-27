package bla.nalovma.com.booklistingapp;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();

    /* URl from Google Books API. */
    private static final String REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=%s&maxResults=%s";

    /** Adapter for list of books */
    private BookAdapter bookAdapter;

    /** TextView displayed with different text:
     * provide the user to enter name of book want to search for.
     * No internet access.
     * Result is empty, No books found.
     */
    private TextView mEmptyStateTV;

    /** ProgressBar shows loading while user clicks on search button */
    private ProgressBar mLoadingProgress;

    /** Search button */
    private Button mSearchButton;

    /** ListView of books */
    private ListView mBooksListView;

    /** save state and restore the list back to the previously scrolled position */
    Parcelable state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // calling method initViews to start initializing the views
        initViews();

        //restore the list back to the previously scrolled position if state not empty
        if(state != null) {
            mBooksListView.onRestoreInstanceState(state);
        }

        // TextView message to ask the user enter the book name want to search
        showErrorMessage("Write a book name to search");
    }

    //set the text of TextView when there is error or no results
    private void showErrorMessage(String message) {

        // Enable search button
        mSearchButton.setEnabled(true);

        // stop loading
        mLoadingProgress.setVisibility(View.GONE);

        // show the error message
        mEmptyStateTV.setVisibility(View.VISIBLE);
        mEmptyStateTV.setText(message);
    }

    // when search result not empty
    private void showContent(List<Book> books) {
        // Enable search button
        mSearchButton.setEnabled(true);

        // stop loading
        mLoadingProgress.setVisibility(View.GONE);

        // hide TextView
        mEmptyStateTV.setVisibility(View.GONE);

        // add the search results to the adapter, this will update the listView
        bookAdapter.addAll(books);
    }

    // initializing the views
    private void initViews() {
        mBooksListView = findViewById(R.id.books_list);

        // create book adapter and add empty list
        bookAdapter = new BookAdapter(this, new ArrayList<Book>());

        // Set adapter to the listView
        mBooksListView.setAdapter(bookAdapter);

        mEmptyStateTV = findViewById(R.id.empty_message);
        mLoadingProgress = findViewById(R.id.loadingProgressBar);
        final EditText mSearchEditText = findViewById(R.id.search_editText);

        mSearchButton = findViewById(R.id.search_button);

        // initialize the on click button for the search button
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // clean the book adapter
                bookAdapter.clear();

                //check if no internet access show error message and
                if (!checkConnectivity()) {
                    showErrorMessage("No internet access");
                    return;
                }

                // get the text from the search TexView
                String searchText = mSearchEditText.getText().toString();

                // if the search text not empty
                if (!TextUtils.isEmpty(searchText)) {
                    try {
                        // get 10 results from the api
                        String resultNum = "10";

                        //create the search request
                        String request = String.format(REQUEST_URL, searchText, resultNum);

                        // show loading
                        mLoadingProgress.setVisibility(View.VISIBLE);

                        // make the search button disabled
                        mSearchButton.setEnabled(false);

                        // start the search task
                        new SearchBooks().execute(request);
                    } catch (Exception e) {
                        Log.d(LOG_TAG, e.getMessage());
                    }

                }
            }
        });
    }

    @Override
    protected void onPause() {
        // save the listView state when the app paused or on rotation
        state = mBooksListView.onSaveInstanceState();
        super.onPause();
    }

    // task to load the search results on the background
    private class SearchBooks extends AsyncTask<String, Void, List<Book>> {

        @Override
        protected List<Book> doInBackground(String... urls) {
            if (urls[0] == null || urls[0].length() < 1) {
                return null;
            }

            // create the connection and fetch the data from the api
            List<Book> bookList = QueryUtils.fetchBookData(urls[0]);
            return bookList;
        }

        @Override
        protected void onPostExecute(List<Book> books) {

            //return books list if the result not empty, and show the error message when there is no results
            if (books != null && !books.isEmpty()) {
                showContent(books);
            } else {
                showErrorMessage("No books found");
            }
        }
    }

    // this method checks the connectivity and return true when it's connected and false when it's not connected
    private boolean checkConnectivity() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

}
