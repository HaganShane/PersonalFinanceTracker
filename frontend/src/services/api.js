// api.js
// Central Axios configuration for all API calls to the Spring Boot backend.
// Base URL points to localhost:8080 where the Spring Boot app runs locally.
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

export default api;
