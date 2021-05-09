function getRandomInt(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
}

function getRandomDate() {
    var nr_days1 = 51 * 365;
    var nr_days2 = 49 * 365;

    // milliseconds in one day
    var one_day = 1000 * 60 * 60 * 24

    // get a random number of days passed between 2018 and 2021
    var days = getRandomInt(nr_days2, nr_days1);

    return new Date(days * one_day)
}

function insertData() {
    // insert 500,000 documents in one run
    for (var upper_i = 0; upper_i < 5; upper_i++) {

        let documents = []

        for (var i = 1; i <= 100000; i++) {
            let isMovie = (getRandomInt(0, 1)) == 1

            let movieName = null
            let episodeName = null
            let showName = null

            if (isMovie) {
                movieName = "movie" + getRandomInt(100, 10000)
            }
            else {
                showName = "show" + getRandomInt(100, 10000)
                episodeName = "episode" + getRandomInt(100, 10000)
            }

            let d = getRandomDate()

            let eventType = "UNKNOWN"
            let eventRandom = getRandomInt(0, 3)

            switch (eventRandom) {
                case 0: eventType = "PLAY"; break;
                case 1: eventType = "PAUSE"; break;
                case 2: eventType = "RESUME"; break;
                case 3: eventType = "STOP"; break;
            }

            let document = {
                userName: "name" + i,
                userId: i.toString(),
                timestamp: d,
                serverName: "plex-server",
                serverId: "server-guid",
                year: d.getFullYear(),
                eventType: eventType,
                movieName: movieName,
                episodeName: episodeName,
                showName: showName
            }

            documents.push(document)
        }

        db.plexHooks.insertMany(documents)
    }
}