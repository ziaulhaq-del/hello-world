#!/usr/bin/env python3

import configparser
import subprocess

config_file = "servers.conf"

# Read the configuration file
config = configparser.ConfigParser(allow_no_value=True)
config.read(config_file)

# Create a dictionary to store servers grouped by application and environment
servers_by_application = {}

# Populate the dictionary with servers grouped by application and environment
for section in config.sections():
    if "-" in section:
        application, environment = section.split('-', 1)
        servers = [entry for entry in config[section]]
        if application not in servers_by_application:
            servers_by_application[application] = {}
        if environment not in servers_by_application[application]:
            servers_by_application[application][environment] = []
        servers_by_application[application][environment].extend(servers)

# Display the list of unique applications
if not servers_by_application:
    print("No servers found in the configuration file.")
    exit(1)

print("Available applications:")
unique_applications = list(servers_by_application.keys())
for i, app in enumerate(unique_applications, start=1):
    print(f"{i} - {app}")

# Prompt for user input to choose an application
try:
    application_index = int(input("Choose an application (enter the number): "))
except ValueError:
    print("Invalid input. Please enter a valid number.")
    exit(1)

# Validate user input
if 1 <= application_index <= len(unique_applications):
    selected_application = unique_applications[application_index - 1]
    print(f"Application: {selected_application}")

    # Display environments and servers for the selected application
    print("Available environments and servers:")
    environments = list(servers_by_application[selected_application].keys())
    for env in environments:
        print(f"\n{env}:")
        servers_in_env = servers_by_application[selected_application][env]
        for i, server in enumerate(servers_in_env, start=1):
            print(f"{i + len(servers_in_env) * (environments.index(env))} - {server}")

    # Prompt for user input to choose a server
    try:
        server_index = int(input("Choose a server (enter the number): "))
    except ValueError:
        print("Invalid input. Please enter a valid number.")
        exit(1)

    # Validate user input
    valid_indices = list(range(1, sum(len(servers) for servers in servers_in_env) + 1))
    if server_index in valid_indices:
        for env in environments:
            servers_in_env = servers_by_application[selected_application][env]
            if server_index <= len(servers_in_env):
                chosen_environment = env
                chosen_server = servers_in_env[server_index - 1]

                print(f"Connecting to {chosen_environment} environment, server: {chosen_server}")

                # Connect to the selected server via SSH
                subprocess.run(["ssh", chosen_server])
                break
            else:
                server_index -= len(servers_in_env)
    else:
        print("Invalid input. Please choose a valid number.")
        exit(1)
else:
    print("Invalid input. Please choose a valid number.")
    exit(1)
    