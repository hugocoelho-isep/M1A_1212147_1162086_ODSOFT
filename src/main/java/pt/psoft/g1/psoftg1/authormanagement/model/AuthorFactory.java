package pt.psoft.g1.psoftg1.authormanagement.model;

import pt.psoft.g1.psoftg1.authormanagement.api.AuthorView;

public class AuthorFactory {

    public static Author createAuthor(String name, String bio, String photoURI) {
        return new Author(name, bio, photoURI);
    }

    public static Author createAuthor(Long number, String name, String bio, String photoURI) {
        var author =  new Author(name, bio, photoURI);
        author.setAuthorNumber(number);
        return new Author(name, bio, photoURI);
    }

    public static AuthorView createAuthorView(Author author) {
        AuthorView authorView = new AuthorView();
        authorView.setAuthorNumber(author.getAuthorNumber());
        authorView.setName(author.getName());
        authorView.setBio(author.getBio());
        return authorView;
    }
}