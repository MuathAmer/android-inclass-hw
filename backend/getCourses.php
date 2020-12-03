<?php


header('content-type: application/json');

getmethod();

function getmethod()
{
   include "db.php";
   $sql = "SELECT * FROM Course";
   $result = mysqli_query($conn, $sql);
   $result_str = "";

   if (mysqli_num_rows($result) > 0) {
      $rows = array();
      while ($r = mysqli_fetch_assoc($result)) {
         $result_str .= implode("\t",$r) . "\n";
         $rows["result"][] = $r;
      }

      echo $result_str;
      // echo json_encode($rows);
   }
}
