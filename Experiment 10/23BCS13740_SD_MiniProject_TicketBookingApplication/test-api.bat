@echo off
REM Ticket Booking System - Windows API Testing Script

setlocal enabledelayedexpansion

set BASE_URL=http://localhost:8080/api

echo.
echo ========================================
echo Ticket Booking System - API Test
echo ========================================
echo.

REM Test 1: Create a Show
echo [1] Creating a Show...
for /f %%A in ('powershell -NoProfile -Command "Invoke-RestMethod -Uri %BASE_URL%/shows -Method POST -Headers @{'Content-Type'='application/json'} -Body '{\"movieName\":\"Inception\",\"showTime\":\"2026-04-25T18:00:00\",\"totalSeats\":80}' | ConvertTo-Json"') do (
    echo %%A
)
echo.

REM Test 2: Get All Shows
echo [2] Getting all shows...
powershell -NoProfile -Command "Invoke-RestMethod -Uri %BASE_URL%/shows -Method GET | ConvertTo-Json | Write-Host"
echo.

echo.
echo ========================================
echo Tests completed!
echo ========================================
echo.
echo To run individual tests, use curl directly:
echo.
echo   1. Create Show:
echo      curl -X POST http://localhost:8080/api/shows ^
echo        -H "Content-Type: application/json" ^
echo        -d "{\"movieName\":\"Inception\",\"showTime\":\"2026-04-25T18:00:00\",\"totalSeats\":80}"
echo.
echo   2. Get Shows:
echo      curl http://localhost:8080/api/shows
echo.
echo   3. Get Seats:
echo      curl http://localhost:8080/api/seats/1
echo.
echo   4. Book Seat:
echo      curl -X POST "http://localhost:8080/api/book?seatId=1&userId=100"
echo.
echo   5. Concurrent Booking (Stress Test):
echo      curl -X POST "http://localhost:8080/api/book/simulate?seatId=2"
echo.

