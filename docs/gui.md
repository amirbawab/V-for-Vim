<div>
    <h3>Connect to Middleware Server</h3>
    <table>
        <tr>
            <th>Server IP</th>
            <td><input name="ip" type="text"/></td>
        </tr>
        <tr>
            <th>Server port</th>
            <td><input name="port" type="text"/></td>
        </tr>
        <tr>
            <td></td>
            <td><button class="connect" onClick="connect()">Connect</button></td>
        </tr>
    </table>

    <h3>Architecture</h3>
    <canvas style="overflow: auto;" id="arch" width="700" height="500" style="border:1px solid #000000;"></canvas>
</div>

<script>
    window.addEventListener('load', function() {
        var socket =  io.connect('http://localhost:3000');
        socket.on('connect', function() {
            output("Connected!");
        });
        
        socket.on('chatevent', function(data) {
            output("Hello");
        });
        
        socket.on('disconnect', function() {
            output("Disconnected");
        });

        function sendDisconnect() {
                socket.disconnect();
        }

        function sendMessage() {
            var jsonObject = {message: "Sent from JS"};
            socket.emit('chatevent', jsonObject);
        }

        function output(message) {
            console.log(message);
        }
        
    });
</script>
