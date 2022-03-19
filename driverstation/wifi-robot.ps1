# Connects the WiFi adapter to the robot, setting its IP appropriately.

$ip = '10.85.12.100'
$maskBits = 8  # ie. '255.0.0.0'

$adapter = Get-NetAdapter Wi-Fi

$adapter | New-NetIPAddress `
  -AddressFamily 'IPv4' `
  -IPAddress $ip `
  -PrefixLength $maskBits `

# Connect to the robot (it's a WiFi network called '8512').
netsh wlan connect name=8512 ssid=8512

