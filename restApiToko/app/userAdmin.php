<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class userAdmin extends Model
{
    public function user(){
        return $this->hasOne('App\User','id','user_id'); //(nama kolom dituju, id yg dituju, id model ini)
    }
}
