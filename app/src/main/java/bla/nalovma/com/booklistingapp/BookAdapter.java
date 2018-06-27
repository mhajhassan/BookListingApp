package bla.nalovma.com.booklistingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/*
creates a list item layout for books, this will be used in a listView
 */
public class BookAdapter extends ArrayAdapter<Book> {

    /**
     * Constructs a new {@link BookAdapter}.
     */
    public BookAdapter(@NonNull Context context, List<Book> books) {
        super(context, 0, books);
    }


    /**
     * returns list item view that display books information
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.book_list_item, parent, false);
        }

        // get the book in this position
        Book currentBook = getItem(position);

        TextView titleView = view.findViewById(R.id.title_tv);
        TextView authorsView = view.findViewById(R.id.authors_tv);
        TextView publishDateView = view.findViewById(R.id.publish_date_tv);
        ImageView bookCoverView = view.findViewById(R.id.book_cover);

        // set the title textView from current book
        titleView.setText(currentBook.getTitle());

        //set the authors textView from current book
        authorsView.setText(currentBook.getAuthors());

        //set the publish date textView from current book
        publishDateView.setText(currentBook.getPublishDate());

        // get the url of the book cover and load it in the imageView
        String url = currentBook.getBookCover();
        if (!url.isEmpty()) {
            Glide.with(getContext())
                    .load(url)
                    .into(bookCoverView);
        }

        return view;
    }
}
