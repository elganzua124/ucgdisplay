cmake_minimum_required(VERSION 3.9)
project(ucgd-mod-input)

include(../../../../../include/CMakeCommon.txt)

if (NOT UNIX)
    message(FATAL_ERROR "Only unix/linux system builds are currently supported by this module")
endif ()

# Find Libevent Package
if (UNIX)
    find_package(LibEvent REQUIRED)
    find_package(Threads REQUIRED)
    set(THREADS_PREFER_PTHREAD_FLAG ON)
    set(THREADS_PTHREAD_ARG "2" CACHE STRING "Forcibly set by CMakeLists.txt." FORCE)

    list(APPEND UCGDISP_HDR "InputDevHelper.h" "InputDeviceManager.h" "InputEventManager.h" "CommSpi.h")
    list(APPEND UCGDISP_SRC "InputDevHelper.cpp" "InputDeviceManager.cpp" "InputEventManager.cpp" "CommSpi.cpp")
    add_executable(ucginput-test "InputDevHelper.h" "InputEventManager.h" "InputDevHelper.cpp" "InputEventManager.cpp" "InputDeviceTest.cpp")
    target_compile_options(ucginput-test PRIVATE -Wno-write-strings)
endif ()

list(APPEND UCGINPUT_HDR "../../../../../include/Global.h" "InputDevHelper.h" "InputDeviceManager.h" "InputEventManager.h")
list(APPEND UCGINPUT_SRC "../../../../../include/Global.cpp" "InputDevHelper.cpp" "InputDeviceManager.cpp" "InputEventManager.cpp")

add_library(ucginput SHARED ${UCGINPUT_HDR} ${UCGINPUT_SRC})
target_compile_options(ucginput PRIVATE -Wno-write-strings)

# Add libevent dependency (called usually if the library was built from the repository)
if (UNIX AND (LibEvent_FOUND AND TARGET libevent))
    message(STATUS "[LIBEVENT] Found 'libevent' target. Adding as dependency")
    list(APPEND INC_DIRS ${LIBEVENT_INCLUDE_DIRS} Threads::Threads)
    list(APPEND LINK_LIBS ${LIBEVENT_LIBRARIES})
    add_dependencies(ucginput libevent)
    add_dependencies(ucginput-test libevent)
    target_link_libraries(ucginput-test ${LINK_LIBS})
endif ()

#[[if (WIN32)
    message(STATUS "[target_link_libraries] Setting MINGW dependencies static")
    target_link_libraries(ucginput ${LINK_LIBS} -static-libgcc -static-libstdc++ -static)
else ()
    target_link_libraries(ucginput ${LINK_LIBS})
endif ()]]

include_directories(${INC_DIRS})
target_link_libraries(ucginput ${LINK_LIBS})