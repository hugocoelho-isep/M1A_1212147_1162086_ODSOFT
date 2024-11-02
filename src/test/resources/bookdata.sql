INSERT INTO author (author_number, name, bio, version) VALUES (1, 'Jane Doe', 'Author of renowned books', 1);
INSERT INTO author (author_number, name, bio, version) VALUES (2, 'John Smith', 'Author of another book', 1);


INSERT INTo genre (PK, GENRE) VALUES (1, 'Fantasia');

INSERT INTO book (PK, title, GENRE_PK, DESCRIPTION, ISBN, VERSION) VALUES (1, 'Book 1', 1, 'A book about something', '9782826012092', 1);
INSERT INTO book (PK, title, GENRE_PK, DESCRIPTION, ISBN, VERSION) VALUES (2, 'Book 2', 1, 'A book about something else', '9782826012093', 1);

INSERT INTO BOOK_AUTHORS (book_PK, AUTHORS_AUTHOR_NUMBER) VALUES (1, 1);
INSERT INTO BOOK_AUTHORS (book_PK, AUTHORS_AUTHOR_NUMBER) VALUES (2, 1);