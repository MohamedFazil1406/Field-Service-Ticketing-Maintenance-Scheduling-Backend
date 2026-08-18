CREATE TABLE tickets (
                         id BIGSERIAL PRIMARY KEY,

                         title VARCHAR(200) NOT NULL,

                         description TEXT NOT NULL,

                         device_id BIGINT NOT NULL,

                         assigned_technician_id BIGINT,

                         priority VARCHAR(20) NOT NULL,

                         status VARCHAR(20) NOT NULL,

                         weather_risk VARCHAR(20) NOT NULL,

                         sla_deadline TIMESTAMP,

                         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                         updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                         CONSTRAINT fk_tickets_device
                             FOREIGN KEY (device_id)
                                 REFERENCES devices(id),

                         CONSTRAINT fk_tickets_technician
                             FOREIGN KEY (assigned_technician_id)
                                 REFERENCES users(id)
);