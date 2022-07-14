# Airline Demo Microservice
The module contains functionality for the Airlines Management System

##Testing the API with Postman

###Adding an airline to the system
`POST /airlines`
```json
{
    "name": "Belavia",
    "initialBudget": "150.50",
    "baseLocation": {
        "name": "Minsk",
        "location": {
            "latitude": 53.893009,
            "longitude": 27.567444
        }
    }
}
```

###Retrieving a list of airlines and their current balance
`GET /airlines`

###Adding an aircraft to a specific airline
`POST /airlines/1/aircrafts`
```json
{
    "price": 105.50,
    "maxDistance": 800
}
```

###Selling an aircraft
`POST /aircrafts/1/sell`

###Adding a destination
`POST /airlines/1/destinations`
```json
{
    "name": "Minsk",
    "location": {
        "latitude": 53.893009,
        "longitude": 27.567444
    }
}
```

###Listing the distance of a given airline from all the destinations in the system
`GET /airlines/1/destinations/routes`

###Listing the available destinations for a given airline
`GET /airlines/1/destinations/routes?availableOnly=true`

###Buying an aircraft from another airline
`POST /aircrafts/1/buy`
```json
{
    "buyerAirlineId": "2"
}
```