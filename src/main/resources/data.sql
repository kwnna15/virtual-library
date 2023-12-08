insert into BOOK_DTO (isbn,title,author,genre,quantity) values
('9789113071299','Män som hatar kvinnor', 'Stieg Larsson', 'Drama',2),
('9789113071305','flickan som lekte med elden', 'Stieg Larsson', 'Fiction',2),
('9789175031743','En man som heter Ove', 'Fredrik Backman', 'Humor',3),
('9789113047041','Torka aldrig tårar utan handskar', 'Jonas Gardell', 'Humor',3),
('9789173133784','En komikers uppväxt', 'Jonas Gardell', 'Humor',3),
('9789176631874','Nils Holgerssons underbara resa', 'Selma Lagerlöf', 'Children',3),
('9789188680297','Kejsaren av Portugallien', 'Selma Lagerlöf', 'Children',3),
('9789177817963','Mårbackasviten', 'Selma Lagerlöf', 'Drama',2),
('9789198114577','Moln', 'Karin Boye', 'Fiction',3),
('9789188680358','Hemsöborna', 'August Strindberg', 'Fiction',2),
('9789198329643','Röda rummet', 'August Strindberg', 'Drama',2),
('9789164204653','Sprängaren', 'Lisa Marklund', 'Drama',3),
('9789164204707','Nobels testamente', 'Lisa Marklund', 'History',3),
('9789164204660','Studio sex', 'Lisa Marklund', 'Fiction',2),
('9789187707469','En väktares bekännelser', 'Elin Säfström', 'Memoir',2)
;

insert into USER_DTO (name,address) values
('Maria Bello','Trees Street 13'),
('Kent Blah','Sveavagen 57')
('Joel','Lidnersplan 57')
('Konna','Sveavagen 45')
;

insert into BOOK_LOAN_REGISTER_DTO (userId,bookId,startDate,endDate) values
(1,3, "31/12/2023", "04/01/2024"),
(2,4, "28/12/2023", "05/01/2024"),
(4,10, "15/12/2023", "04/01/2024")
;