
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

DROP TABLE IF EXISTS termofservice_user_accepted;
CREATE TABLE termofservice_user_accepted (
id_user_accepted int AUTO_INCREMENT,
guid varchar(255) default '' NOT NULL,
fk_id_entry int default '0' NOT NULL,
date_accepted date NOT NULL,
version int default '0',
PRIMARY KEY (id_user_accepted),
INDEX (guid)
);