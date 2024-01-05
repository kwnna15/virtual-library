create table BOOK_DTO(
    ID int not null AUTO_INCREMENT,
    ISBN varchar(100) not null,
    TITLE varchar(100) not null,
    AUTHOR varchar(100) not null,
    GENRE varchar(100) not null,
    QUANTITY int not null,
    PRIMARY KEY (ID)
);


create table USER_DTO(
    ID int not null AUTO_INCREMENT,
    NAME varchar(100) not null,
    ADDRESS varchar(100) not null,
    PRIMARY KEY (ID)
);

create table BOOK_LOAN_REGISTER_DTO(
    ID int not null AUTO_INCREMENT,
    USER_ID int not null,
    BOOK_ID int not null,
    START_DATE varchar(100) not null,
    END_DATE varchar(100) not null,
    PRIMARY KEY (ID)
);