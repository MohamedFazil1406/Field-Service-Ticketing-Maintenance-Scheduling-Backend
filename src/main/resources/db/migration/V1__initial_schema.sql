CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       role VARCHAR(20) NOT NULL,
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                       CONSTRAINT chk_users_role
                           CHECK (role IN ('ADMIN', 'DISPATCHER', 'TECHNICIAN'))
);


CREATE TABLE sites (
                       id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(150) NOT NULL,
                       address VARCHAR(255) NOT NULL,
                       latitude DOUBLE PRECISION NOT NULL,
                       longitude DOUBLE PRECISION NOT NULL,
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE devices (
                         id BIGSERIAL PRIMARY KEY,
                         device_code VARCHAR(100) NOT NULL UNIQUE,
                         name VARCHAR(150) NOT NULL,
                         site_id BIGINT NOT NULL,
                         status VARCHAR(30) NOT NULL,
                         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                         CONSTRAINT fk_devices_site
                             FOREIGN KEY (site_id)
                                 REFERENCES sites(id)
);


CREATE TABLE tickets (
                         id BIGSERIAL PRIMARY KEY,
                         title VARCHAR(200) NOT NULL,
                         description TEXT NOT NULL,

                         device_id BIGINT NOT NULL,

                         assigned_technician_id BIGINT,

                         priority VARCHAR(20) NOT NULL,
                         status VARCHAR(30) NOT NULL,

                         sla_deadline TIMESTAMP NOT NULL,

                         weather_risk VARCHAR(20) NOT NULL DEFAULT 'NONE',

                         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                         CONSTRAINT fk_tickets_device
                             FOREIGN KEY (device_id)
                                 REFERENCES devices(id),

                         CONSTRAINT fk_tickets_technician
                             FOREIGN KEY (assigned_technician_id)
                                 REFERENCES users(id),

                         CONSTRAINT chk_ticket_priority
                             CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH', 'CRITICAL')),

                         CONSTRAINT chk_ticket_status
                             CHECK (status IN (
                                               'OPEN',
                                               'ASSIGNED',
                                               'IN_PROGRESS',
                                               'RESOLVED',
                                               'CLOSED',
                                               'ESCALATED'
                                 )),

                         CONSTRAINT chk_weather_risk
                             CHECK (weather_risk IN ('NONE', 'CAUTION', 'SEVERE'))
);


CREATE INDEX idx_tickets_technician
    ON tickets(assigned_technician_id);

CREATE INDEX idx_tickets_status
    ON tickets(status);

CREATE INDEX idx_tickets_sla_deadline
    ON tickets(sla_deadline);

CREATE INDEX idx_devices_site
    ON devices(site_id);