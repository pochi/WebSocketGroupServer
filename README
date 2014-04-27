# WebSocketGroupServer

This application realize websocket group message chat using jsr356's websocket implementation.

## Install and start

You can execute below.
Jetty run port 8080.

```
$ git clone https://github.com/pochi/WebSocketGroupServer
$ cd WebSocketGroupServer
$ mvn jetty:run
```

## Try client

For example, it try ruby client.

```ruby
require 'websocket-client-simple'

ws = WebSocket::Client::Simple.connect 'ws://localhost:8080/events/?1'

ws.on :open do
  puts 'connect succeed!'
end

ws.on :message do |message|
  puts "come message: " + message.data()
end

ws.on :error do |e|
  puts e.inspect
end

count = 0

loop do
  count += 1
  ws.send count.to_s
  sleep 1
end
```

When it runs, result is below

```
pochi 21:56:43 % ruby connect.rb                                                                            /opt/local/repos/ruby/jetty-connect-sample
connect succeed!
come message: 2
come message: 3
come message: 4
come message: 5
.
.
.
pochi 21:56:43 %
```