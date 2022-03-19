# Connects to ManzanoRobots2.4, the wireless network at the tech center.

$adapter = Get-NetAdapter Wi-Fi
$interface = $adapter | Get-NetIPInterface -AddressFamily 'IPv4'

# Reconfigure DHCP if it's disbaled
If ($interface.Dhcp -eq "Disabled") {
  # Remove the default gateway
  If (($interface | Get-NetIPConfiguration).Ipv4DefaultGateway) {
    $interface | Remove-NetRoute -Confirm:$false
  }

  # Enable DHCP, clear DNS servers
  $interface | Set-NetIPInterface -DHCP Enabled
  $interface | Set-DnsClientServerAddress -ResetServerAddresses
}

# Connect to the tech center WiFi
netsh wlan connect name=MonzanoRobots2.4 ssid=MonzanoRobots2.4

