package bla.nalovma.com.booklistingapp;

public class Book {

    /** Title of the book */
    private String mTitle;

    /** Url of the book cover */
    private String mBookCover;

    /** Authors of the book */
    private String mAuthors;

    /** Publish date of the book */
    private String mPublishDate;

    /** Constructor a new book */
    public Book(String mTitle, String mBookCover, String mAuthors, String mPublishDate) {
        this.mTitle = mTitle;
        this.mBookCover = mBookCover;
        this.mAuthors = mAuthors;
        this.mPublishDate = mPublishDate;

    }

    /**
     * Returns the title of the book.
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * Returns the book cover url of the book.
     */
    public String getBookCover() {
        return mBookCover;
    }

    /**
     * Returns the authors of the book.
     */
    public String getAuthors() {
        return mAuthors;
    }

    /**
     * Returns the publish date of the book.
     */
    public String getPublishDate() {
        return mPublishDate;
    }

}
