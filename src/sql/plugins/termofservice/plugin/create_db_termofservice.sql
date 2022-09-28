
--
-- Structure for table termofservice_entry
--

DROP TABLE IF EXISTS termofservice_entry;
CREATE TABLE termofservice_entry (
id_entry int AUTO_INCREMENT,
text long varchar NOT NULL,
accepted SMALLINT,
PRIMARY KEY (id_entry)
);
