function MainAppModel() {
    var self = this;

    self.myLogin;

    self.rooms = ko.observableArray();

    self.subsocket;

    self.connect = function (targetUrl) {
        var socket = $.atmosphere;
        var transport = 'long-polling';
        var roomListUrl = targetUrl;

        var request = {
            url: roomListUrl + "/atm",
            contentType: "application/json",
            logLevel: 'debug',
            //shared : 'true',
            transport: transport,
            fallbackTransport: 'long-polling',
            maxRequest: 10000000,
            //reconnectInterval: 10000,
            //callback: callback,
            onMessage: self.onMessage,
            onOpen: function (response) {
                console.log('Atmosphere onOpen: Atmosphere connected using ' + response.transport);
                transport = response.transport;

                // For loading all available rooms.
                $.get(roomListUrl + "/all", function (data, status) {
                    console.log("Data received with ajax: " + data + ". Status: " + status);
                    // Crutch.
                    self.onMessage({"responseBody": data});
                });
            },
            onReconnect: function (request, response) {
                console.log("Atmosphere onReconnect: Reconnecting");
            },
            onClose: function (response) {
                console.log('Atmosphere onClose executed');
            },

            onError: function (response) {
                console.log('Atmosphere onError: Sorry, but there is some problem with your '
                + 'socket or the server is down');
            }
        };

        self.subSocket = socket.subscribe(request);
    };

    self.onMessage = function (response) {

        var messages = response.responseBody;
        var messagesObj;

        try {
            messagesObj = $.parseJSON(messages);
        } catch (e) {
            console.log("An error occurred while parsing the JSON Data: " + messages.data + "; Error: " + e);
            return;
        }

        if (messagesObj == null || !messagesObj.hasOwnProperty("length")) {
            console.log("No object returned or no class specified.");
            return;
        }

        for (var i = 0; i < messagesObj.length; i++) {
            var resultItem = messagesObj[i];

            if (!resultItem.hasOwnProperty("@class")) {
                console.log("Wrong message format." + resultItem);
                continue;
            }

            var resultType = resultItem['@class'];
            console.log('Object type returned: ' + resultType);

            if (resultType == "RoomMessage") {
                self.receiveRoomMessage(new RoomMessage(resultItem));
            } else if (resultType == "RoomListMessage") {
                self.receiveRoomListMessage(new RoomListMessage(resultItem));
            } else {
                throw "resultType " + resultType + " is not handled.";
            }
        }
    };

    self.disconnect = function () {
        self.subSocket.unsubscribe();
    };

    self.receiveRoomMessage = function (message) {
        if (message.action == "added") {
            self.roomAdded(message.room);
        }

        if (message.action == "removed") {
            self.roomRemoved(message.room);
        }
    };

    self.roomAdded = function (room) {
        self.rooms.push(room);
    };

    self.roomRemoved = function (room) {
        var r = self.rooms.remove(
            function (item) {
                return item.id != room.id;
            }
        );
        self.rooms(r);
    };

    self.receiveRoomListMessage = function (roomListMessage) {
        for (var i = 0; i < roomListMessage.rooms.length; i++) {
            self.roomAdded(new Room(roomListMessage.rooms[i]));
        }
    }
}

function Room(data) {
    var self = this;

    self.id = data.id;
    self.title = data.title;
}

function RoomMessage(data) {
    var self = this;
    self["@class"] = "RoomMessage";

    self.action = data.action;
    self.room = new Room(data.room);
}

function RoomListMessage(data) {
    var self = this;
    self["@class"] = "RoomListMessage";

    self.rooms = data.rooms;
}