create table BOOK_DTO(
    ID int not null AUTO_INCREMENT,
    ISBN varchar(100) not null,
    TITLE varchar(100) not null,
    AUTHOR varchar(100) not null,
    GENRE varchar(100) not null,
    PRIMARY KEY (ID)
);