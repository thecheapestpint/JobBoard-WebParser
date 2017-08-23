# Job Board Scraper

## **Work In Progress**
- Documentation needs work.
- Comment the code
- Refactor the code
- Overall cleanup of EVERYTHING

### The future
- Create a web crawler to dynamically add job boards to the project.
- Expiry system: Delete the jobs after 3-5 days after scraping them.

## **Overview**

The aim of this project is to scrape multiple job boards and display them all in a client.
How this differs compared to other scrapers is that it only looks for jobs which contains the words entered in the "keywords" fiield meaning every job should contain the title you're looking for.
No more searching for Java Developer and getting Dev Ops popping up.



## **What's being used**
  - Drop Wizard
  - MongoDB
  - JSoup
  - PhantomJS
 
## How to use

To use the parser, either build the application with Maven or use the .jar file that's within the target folder.
Before doing this, make sure you have a MySQL server and a Mongo DB server running with the configuration found below.

Within your terminal find the .jar file and enter the command: 
```
java -jar DropWizardScraper-1.0-SNAPSHOT server search.yml
```

## Job Board Configuration
To make things easier, the scraper will search for elements on the page based on the data put into Mongo DB. Within the database, a collection called website_config should be made which will contain all these configurations which would be in the form of:

```
{
    "_id" : ObjectId("5973b7408acebd86fa11dc72"),
    "websiteURL" : "https://www.jobsite.co.uk",
    "searchForm" : {
        "form" : {
            "tag" : "form",
            "id" : "psformnew"
        },
        "keyword" : {
            "tag" : "input",
            "name" : "jobTitle-input",
            "id" : "jobTitle-input"
        },
        "location" : {
            "tag" : "input",
            "name" : "location-input",
            "id" : "location-input"
        }
    },
    "page" : {
        "jobContainer" : {
            "tag" : "div",
            "class" : "vacancy_search_result"
        },
        "keyword" : {
            "tag" : "li",
            "class" : "vacancy_section title"
        },
        "location" : {
            "tag" : "li",
            "class" : "location"
        },
        "salary" : {
            "tag" : "li",
            "class" : "salary"
        },
        "link" : {
            "tag" : "a",
            "href" : "/job/"
        },
        "nextPage" : {
            "tag" : "a",
            "class" : "next"
        }
    }
}
```

At the moment each element is matched by only one attribute, but in the future this can be extended to match more.

### MySQL

For now a PHPmyadmin export of the database will in here giving an example of the database. In the future this will be moved into a script.

```
--
-- Database: `job_search`
--

-- --------------------------------------------------------

--
-- Table structure for table `keywords`
--

CREATE TABLE `keywords` (
  `keyword_id` int(11) NOT NULL,
  `keyword` varchar(128) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `locations`
--

CREATE TABLE `locations` (
  `location_id` int(11) NOT NULL,
  `location` varchar(225) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `search_crawl`
--

CREATE TABLE `search_crawl` (
  `search_id` int(11) NOT NULL,
  `added_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `searched` enum('0','1') NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `search_query`
--

CREATE TABLE `search_query` (
  `search_id` int(11) NOT NULL,
  `keyword_id` int(11) NOT NULL,
  `location_id` int(11) NOT NULL,
  `last_search` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `website`
--

CREATE TABLE `website` (
  `website_id` int(11) NOT NULL,
  `website_url` varchar(255) NOT NULL,
  `website_allowed` enum('0','1') NOT NULL DEFAULT '0',
  `website_first_crawled` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `website_last_crawled` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `keywords`
--
ALTER TABLE `keywords`
  ADD PRIMARY KEY (`keyword_id`);

--
-- Indexes for table `locations`
--
ALTER TABLE `locations`
  ADD PRIMARY KEY (`location_id`);

--
-- Indexes for table `search_query`
--
ALTER TABLE `search_query`
  ADD PRIMARY KEY (`search_id`);

--
-- Indexes for table `website`
--
ALTER TABLE `website`
  ADD PRIMARY KEY (`website_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `keywords`
--
ALTER TABLE `keywords`
  MODIFY `keyword_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;
--
-- AUTO_INCREMENT for table `locations`
--
ALTER TABLE `locations`
  MODIFY `location_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;
--
-- AUTO_INCREMENT for table `search_query`
--
ALTER TABLE `search_query`
  MODIFY `search_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;
--
-- AUTO_INCREMENT for table `website`
--
ALTER TABLE `website`
  MODIFY `website_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;COMMIT;
```

