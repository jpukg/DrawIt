function RoomAppModel(drawingModel) {
    var self = this;

    self.drawingModel = drawingModel;

    self.myLogin;

    self.gameInterval;
    self.turnInterval;
    self.lastTime = new Date();
    self.isGameInProcess = false;

    self.members = ko.observableArray();
    self.notifications = ko.observableArray();

    self.subsocket;

    self.connect = function (targetUrl) {
        var socket = $.atmosphere;
        var transport = 'long-polling';
        var roomUrl = targetUrl;

        var request = {
            url: roomUrl + "/atm",
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
                $.get(roomUrl + "/init", function (data, status) {
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

            if (resultType == "ChatMessage") {
                self.receiveChatMessage(new ChatMessage(resultItem));
            } else if (resultType == "DrawMessage") {
                self.receiveDrawMessage(new DrawMessage(resultItem));
            } else if (resultType == "MemberMessage") {
                self.receiveMemberMessage(new MemberMessage(resultItem));
            } else if (resultType == "MemberListMessage") {
                self.receiveMemberListMessage(new MemberListMessage(resultItem));
            } else if (resultType == "TimeMessage") {
                self.receiveTimeMessage(new TimeMessage(resultItem));
            } else if (resultType == "GameMessage") {
                self.receiveGameMessage(new GameMessage(resultItem));
            } else if (resultType == "WordMessage") {
                self.receiveWordMessage(new WordMessage(resultItem));
            } else if (resultType == "ScoreMessage") {
                self.receiveScoreMessage(new ScoreMessage(resultItem));
            } else if (resultType == "FinalScoreMessage") {
                self.receiveFinalScoreMessage(new FinalScoreMessage(resultItem));
            } else {
                throw "resultType " + resultType + " is not handled.";
            }
        }
    };

    self.logout = function () {
        self.subSocket.unsubscribe();
    };

    self.receiveMemberMessage = function (memberMessage) {
        if (memberMessage.action == "added") {
            self.memberAdded(memberMessage.member);
        }

        if (memberMessage.action == "removed") {
            self.memberRemoved(memberMessage.member);
        }

        if (memberMessage.action == "got_turn") {
            self.notifications.push(new NotificationMessage(
                {"notification": memberMessage.member.login + " got turn now", "mode": "system"}));

            drawingModel.clearCanvas();

            if (memberMessage.member.login == self.myLogin){
                self.setMyTurn(true);
            } else {
                self.setMyTurn(false);
                $('#word').text("Guess the word!");
                $('#message-button').prop( "disabled", false );
            }
        }
    };

    self.memberAdded = function (member) {
        self.notifications.push(new NotificationMessage({"notification": member.login + " joined the room",
            "mode" : "system"}));
        self.members.push(member);
    };

    self.memberRemoved = function (member) {
        self.notifications.push(new NotificationMessage({"notification": member.login + " left the room",
            "mode" : "system"}));
        var r = self.members.remove(
            function (item) {
                return item.login != member.login;
            }
        );
        self.members(r);
    };

    self.receiveMemberListMessage = function (memberListMessage) {
        for (var i = 0; i < memberListMessage.members.length; i++) {
            self.memberAdded(new Member(memberListMessage.members[i]));
        }
    };

    self.receiveChatMessage = function (chatMessage) {
        self.notifications.push(new NotificationMessage({"notification":chatMessage.textFormatted(), "mode" : "chat"}));
    };

    self.sendChatMessage = function (chatMessage) {
        var value = JSON.stringify(chatMessage);
        self.subSocket.push(value);
    };

    self.receiveDrawMessage = self.drawingModel.receiveDrawMessage;

    self.sendDrawMessage = function (drawMessage) {
        var value = JSON.stringify(drawMessage);
        self.subSocket.push(value);
    }

    self.receiveTimeMessage = function (timeMessage) {
        //self.notifications.push(new NotificationMessage({"notification": timeMessage.timeFormatted()}));
        console.log("Time message: " + timeMessage.timeFormatted());
        self.lastTime = timeMessage.time;
    };

    self.receiveGameMessage = function (gameMessage) {
        self.notifications.push(new NotificationMessage({"notification": "Game " + gameMessage.action,
            "mode" : "system"}));
        if (gameMessage.action == "started"){
            self.isGameInProcess = true;
        }
        self.setMyTurn(false);
    };

    self.receiveWordMessage = function (wordMessage) {
        //self.notifications.push(new NotificationMessage({"notification": "word: " + wordMessage.word}));
        $('#word').text("Draw: "+wordMessage.word);
        $('#message-button').prop( "disabled", true );
    };

    self.receiveScoreMessage = function (scoreMessage) {
        self.notifications.push(new NotificationMessage({
            "notification":
            scoreMessage.scoreInfo.member.login + " now has score:  "
            + scoreMessage.scoreInfo.score, "mode" : "system"
        }));
    };

    self.receiveFinalScoreMessage = function (finalScoreMessage) {

        $('#results').css({'display':'block'});
        $('table').css({'display':'none'});
        for (var i = 0; i < finalScoreMessage.scoreInfos.length; i++) {
            self.receiveScoreMessage(new ScoreMessage({"scoreInfo": finalScoreMessage.scoreInfos[i]}));
            $('#results').append(finalScoreMessage.scoreInfos[i].member.login + ': '
                + finalScoreMessage.scoreInfos[i].score+ ' point(-s)<br/>');
        }
    };

    self.setMyTurn = function (myTurn) {
        if (myTurn == true) {
            self.drawingModel.enableMyTurn();
        } else {
            self.drawingModel.disableMyTurn();
        }
    }
}

function Member(data) {
    var self = this;

    self.login = data.login;
}

function MemberMessage(data) {
    var self = this;
    self["@class"] = "MemberMessage";

    self.action = data.action;
    self.member = new Member(data.member);
}

function MemberListMessage(data) {
    var self = this;
    self["@class"] = "MemberListMessage";

    self.members = data.members;
}

function ChatMessage(data) {
    var self = this;
    self["@class"] = "ChatMessage";

    self.from = data.from;
    self.text = data.text;
    self.time = new Date(data.time);

    self.textFormatted = ko.computed(function () {
        var hours = self.time.getHours();
        var minutes = self.time.getMinutes();
        var seconds = self.time.getSeconds();
        hours = hours >= 10 ? hours : "0" + hours;
        minutes = minutes >= 10 ? minutes : "0" + minutes;
        seconds = seconds >= 10 ? seconds : "0" + seconds;
        return hours + ":"
            + minutes + ":"
            + seconds + " - "
            + self.from + " : "
            + self.text;
        //return self.time.getHours() + ":"
        //    + self.time.getMinutes() + ":"
        //    + self.time.getSeconds() + " - "
        //    + self.from + " : "
        //    + self.text;
    });
}

function DrawMessage(data) {
    var self = this;
    self["@class"] = "DrawMessage";

    self.points = data.points;
}

function TimeMessage(data) {
    var self = this;
    self["@class"] = "TimeMessage";

    self.timeDescription = data.timeDescription;
    self.time = new Date(data.time);

    self.timeFormatted = ko.computed(function () {
        var hours = self.time.getHours();
        var minutes = self.time.getMinutes();
        var seconds = self.time.getSeconds();
        hours = hours >= 10 ? hours : "0" + hours;
        minutes = minutes >= 10 ? minutes : "0" + minutes;
        seconds = seconds >= 10 ? seconds : "0" + seconds;
        return self.timeDescription + " at " + hours + ":" + minutes + ":"
            + seconds;
    });
}

function GameMessage(data) {
    var self = this;
    self["@class"] = "GameMessage";

    self.action = data.action;
}

function WordMessage(data) {
    var self = this;
    self["@class"] = "WordMessage";

    self.word = data.word;
}

function ScoreInfo(data) {
    var self = this;

    self.member = new Member(data.member);
    self.score = data.score;
}

function ScoreMessage(data) {
    var self = this;
    self["@class"] = "ScoreMessage";

    self.scoreInfo = new ScoreInfo(data.scoreInfo);
    self.description = data.description;
}

function FinalScoreMessage(data) {
    var self = this;
    self["@class"] = "FinalScoreMessage";

    self.scoreInfos = data.scoreInfos;
    self.description = data.description;
}

function NotificationMessage(data) {
    var self = this;
    self["@class"] = "NotificationMessage";

    self.notification = data.notification;
    self.mode = data.mode;
}