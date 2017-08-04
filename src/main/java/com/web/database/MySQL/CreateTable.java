package com.web.database.MySQL;

/**
 * Created by Mitchell on 04/08/2017.
 */
public class CreateTable {

    String keyword = "CREATE TABLE `keywords` (\n" +
            "  `keyword_id` bigint(20) NOT NULL,\n" +
            "  `keyword` varchar(128) NOT NULL\n" +
            ");";



    String location = "CREATE TABLE `locations` (\n" +
            "  `location_id` int(11) NOT NULL,\n" +
            "  `location` varchar(225) NOT NULL\n" +
            ")";

    String searchQuery = "CREATE TABLE `search_query` (\n" +
            "  `search_id` int(11) NOT NULL,\n" +
            "  `keyword_id` int(11) NOT NULL,\n" +
            "  `location_id` int(11) NOT NULL,\n" +
            "  `last_search` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP\n" +
            ") ";

    String website = "CREATE TABLE `website` (\n" +
            "  `website_id` int(11) NOT NULL,\n" +
            "  `website_url` varchar(255) NOT NULL,\n" +
            "  `website_allowed` enum('0','1') NOT NULL DEFAULT '0',\n" +
            "  `website_first_crawled` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" +
            "  `website_last_crawled` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00'\n" +
            ")";

}

